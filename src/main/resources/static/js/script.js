// Search and fetch word from backend
function searchWord() {
  const word = document.getElementById("wordInput").value.trim();
  const resultDiv = document.getElementById("result");
  const loadingDiv = document.getElementById("loading");

  if (!word) {
    resultDiv.innerHTML = "<p class='text-red-500'>Please enter a word!</p>";
    return;
  }

  loadingDiv.classList.remove("hidden");
  resultDiv.innerHTML = "";

  fetch(`/api/definition?word=${word}`)
    .then(response => response.json())
    .then(data => {
      loadingDiv.classList.add("hidden");
      if (data && data.length > 0) {
        const meaning = data[0].shortdef ? data[0].shortdef.join(", ") : "No short definition found.";
        const partOfSpeech = data[0].fl || "N/A";

        resultDiv.innerHTML = `
          <h2 class="text-xl font-bold text-blue-700 mb-2">${word}</h2>
          <p><strong>Part of Speech:</strong> ${partOfSpeech}</p>
          <p><strong>Definition:</strong> ${meaning}</p>
          <button onclick="saveWord('${word}', \`${meaning}\`)" 
            class="mt-4 bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded">
            Save Word
          </button>
        `;
        saveToRecent(word);
      } else {
        resultDiv.innerHTML = `<p class='text-red-500'>No definition found for "${word}".</p>`;
      }
    })
    .catch(error => {
      loadingDiv.classList.add("hidden");
      resultDiv.innerHTML = "<p class='text-red-500'>Error fetching definition.</p>";
      console.error(error);
    });
}

// Save searched word to MongoDB
function saveWord(word, meaning) {
  fetch('/api/save', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ word: word, meaning: meaning })
  })
    .then(res => res.text())
    .then(msg => {
      alert(`✅ Word saved: ${word}`);
    })
    .catch(err => {
      alert("❌ Failed to save word.");
      console.error(err);
    });
}

// Store in localStorage for recent searches
function saveToRecent(word) {
  let recent = JSON.parse(localStorage.getItem("recentSearches")) || [];
  if (!recent.includes(word)) {
    recent.unshift(word);
    if (recent.length > 5) recent.pop();
    localStorage.setItem("recentSearches", JSON.stringify(recent));
  }
  displayRecent();
}

// Display recent searches
function displayRecent() {
  const recent = JSON.parse(localStorage.getItem("recentSearches")) || [];
  const list = document.getElementById("recentList");
  if (!list) return; // Avoid error on other pages
  list.innerHTML = "";
  recent.forEach(w => {
    const li = document.createElement("li");
    li.textContent = w;
    list.appendChild(li);
  });
}

// Word of the Day (random from DB)
function fetchWordOfTheDay() {
  const wotd = document.getElementById("wotd");
  if (!wotd) return;

  fetch("/api/wotd")
    .then(res => res.json())
    .then(data => {
      if (data && data.word && data.meaning) {
        wotd.innerHTML = `<strong>${data.word}</strong>: ${data.meaning}`;
      } else {
        wotd.innerHTML = "No word available.";
      }
    })
    .catch(err => {
      console.error(err);
      wotd.innerHTML = "Failed to load word of the day.";
    });
}

// On page load
window.onload = function () {
  displayRecent();
  fetchWordOfTheDay();
};
