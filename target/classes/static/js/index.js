const TREE = {
  q1: {
    options: [
      { label: "食べたい", next: "q2_eat" },
      { label: "ぶらぶらする", next: "q2_walk" }
    ]
  },

  // 食べたい → おしゃれ/がっつり
  q2_eat: {
    options: [
      { label: "おしゃれに", next: "q3_stylish" },
      { label: "がっつり", next: "q3_heavy" }
    ]
  },

  // おしゃれに → くつろぐ / 気軽に
  q3_stylish: {
    options: [
      { label: "くつろぐ", next: "q4_relax" },
      { label: "気軽に", next: "q4_casual" }
    ]
  },
  // くつろぐ → 落ちつく / 映える（Fin）
  q4_relax: {
    options: [
      { label: "落ちつく", next: "fin_calm" },
      { label: "映える", next: "fin_photo" }
    ]
  },
  fin_calm: {
    result: "落ちつく（Fin）",
    url: "result.html"
  },
  fin_photo: {
    result: "映える（Fin）",
    url: "result.html"
  },

  // 気軽に → 新しい / レトロ（Fin）
  q4_casual: {
    options: [
      { label: "新しい", next: "fin_new" },
      { label: "レトロ", next: "fin_retro" }
    ]
  },
  fin_new: {
    result: "新しい（Fin）",
    url: "result.html"
  },
  fin_retro: {
    result: "レトロ（Fin）",
    url: "result.html"
  },

  // がっつり → すするか / すすらんか
  q3_heavy: {
    options: [
      { label: "すするか", next: "q4_do" },
      { label: "すすらんか", next: "q4_noodle" }
    ]
  },
  // すするか → あっさり / こってり（Fin）
  q4_do: {
    options: [
      { label: "あっさり", next: "fin_light" },
      { label: "こってり", next: "fin_rich" }
    ]
  },
  fin_light: { 
    result: "あっさり（Fin）",
    url: "result.html"
  },
  fin_rich: { 
    result: "こってり（Fin）",
    url: "result.html"
  },

  // すすらんか → ひかえめに / リッチに（Fin）
  q4_noodle: {
    options: [
      { label: "ひかえめに", next: "fin_small" },
      { label: "リッチに", next: "fin_lux" }
    ]
  },
  fin_small: {
    result: "ひかえめに（Fin）",
    url: "result.html"
  },
  fin_lux: { 
    result: "リッチに（Fin）",
    url: "result.html"
  },

  // ぶらぶらする → のんびり（Fin）/ 発散
  q2_walk: {
    options: [
      { label: "のんびり", next: "fin_chill" },
      { label: "発散", next: "q3_burst" }
    ]
  },
  fin_chill: {
    result: "のんびり（Fin）",
    url: "result.html"
  },

  // 発散 → 神のみぞ知る（Fin）/ 私のみぞ知る（Fin）
  q3_burst: {
    options: [
      { label: "神のみぞ知る", next: "fin_god" },
      { label: "仏のみぞ知る", next: "fin_me" }
    ]
  },
  fin_god: {
    result: "神のみぞ知る（Fin）",
    url: "result.html"
  },
  fin_me: {
    result: "仏のみぞ知る（Fin）",
    url: "result.html"
  }
};


// ===== HTML要素取得 =====
const answerArea = document.getElementById("answerArea");
const backBtn = document.getElementById("backBtn");
const restartBtn = document.getElementById("restartBtn");

// ===== 状態管理 =====
let currentNodeId = "q1";
const history = [];

// ===== 選択肢描画 =====
function renderOptions(nodeId) {
  const node = TREE[nodeId];

  if (!node) {
    answerArea.innerHTML = "<p>エラー：ノードが見つかりません</p>";
    return;
  }

   // ===== Fin に来たら別ページへ =====
  if (node.result && node.url) {
    // 	結果ページへ移動する際、選んだ結果（nodeId）を「tag」という名前でJavaに送る
    location.href = "/result?tag=" + nodeId;
    return;
  }

  // 選択肢ボタン生成
  answerArea.innerHTML = node.options
    .map(opt =>
      `<button type="button" data-next="${opt.next}">${opt.label}</button>`
    )
    .join("");
}

// ===== ボタンクリック（イベント委任）=====
answerArea.addEventListener("click", (e) => {
  const btn = e.target.closest("button");
  if (!btn) return;

  const nextId = btn.dataset.next;
  if (!nextId) return;

  history.push(currentNodeId);
  currentNodeId = nextId;
  renderOptions(currentNodeId);
});

// ===== 戻る =====
backBtn.addEventListener("click", () => {
  if (history.length === 0) return;
  currentNodeId = history.pop();
  renderOptions(currentNodeId);
});

// ===== 最初から =====
restartBtn.addEventListener("click", () => {
  history.length = 0;
  currentNodeId = "q1";
  renderOptions(currentNodeId);
});


// ===== 初期表示 =====
renderOptions(currentNodeId);