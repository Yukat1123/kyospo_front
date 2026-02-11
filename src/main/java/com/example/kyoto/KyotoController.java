package com.example.kyoto;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import jakarta.servlet.http.HttpSession; // セッション管理用

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;           

@Controller
public class KyotoController {

    @Autowired
    private SpotRepository spotRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;
    
    @Autowired
    private SpotService spotService;

    
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // ログイン画面を表示する
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // ログイン処理を実行する
    @PostMapping("/login")
    public String login(@RequestParam String username, 
                        @RequestParam String password, 
                        HttpSession session, 
                        Model model) {
        
        // DBからユーザーを探す
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            // ログイン成功：セッションにユーザー情報を保存
            session.setAttribute("loginUser", userOpt.get());
            return "redirect:/"; // トップページへ
        } else {
            // ログイン失敗
            model.addAttribute("error", "ユーザー名またはパスワードが違います");
            return "login";
        }
    }

    // ログアウト処理
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // セッションを破棄
        return "redirect:/";
    }
    
    @PostMapping("/favorite")
    public String addFavorite(@RequestParam Long spotId, HttpSession session) {
        // セッションからログイン中のユーザー情報を取得
        User loginUser = (User) session.getAttribute("loginUser");
        
        // ログインしていない場合はログイン画面へ（安全策）
        if (loginUser == null) {
            return "redirect:/login";
        }
        
        // すでにお気に入り登録済みかチェック（二重登録を防ぐ）
        if (!favoriteRepository.existsByUserIdAndSpotId(loginUser.getId(), spotId)) {
            Favorite fav = new Favorite();
            fav.setUserId(loginUser.getId());
            fav.setSpotId(spotId);
            favoriteRepository.save(fav);
        }
        
        // 保存後はひとまずトップページへ戻る
        return "redirect:/"; 
    }

    @GetMapping("/result")
    public String result(@RequestParam(name = "tag", required = false) String tag, Model model) {
        if (tag != null) {
            List<Spot> spots = spotRepository.findByTagIdContaining(tag);
            model.addAttribute("spots", spots);
            model.addAttribute("tag", tag); //追加
        }
        return "result";
    }
    
    @GetMapping("/mypage")
    public String mypage(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        // 1. ログインユーザーのお気に入りレコードをすべて取得
        List<Favorite> favorites = favoriteRepository.findByUserId(loginUser.getId());
        
        // 2. お気に入り登録されたスポットのIDリストを作成
        List<Long> spotIds = favorites.stream().map(Favorite::getSpotId).toList();
        
        // 3. スポットIDリストを使って、スポットの詳細情報を一括取得
        List<Spot> favSpots = spotRepository.findAllById(spotIds);
        
        model.addAttribute("favSpots", favSpots);
        return "mypage";
     }
    
    @PostMapping("/favorite/delete")
    public String deleteFavorite(@RequestParam Long spotId, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        
        if (loginUser != null) {
            favoriteRepository.deleteByUserIdAndSpotId(loginUser.getId(), spotId);
        }
        
        // 削除後はマイページに再表示
        return "redirect:/mypage";
    }
    
    @GetMapping("/spots/random")
    public String getRandomSpot(
            @RequestParam String tag,
            Model model
    ) {
        List<Spot> spots = spotService.findByTagIdContaining(tag);

        if (spots.isEmpty()) {
            model.addAttribute("message", "スポットが見つかりません");
            return "random";
        }

        Spot randomSpot = spots.get(new Random().nextInt(spots.size()));
        model.addAttribute("spot", randomSpot);

        return "random";
    }


}