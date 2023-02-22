var topwords = document.querySelectorAll("#cemantix-guesses > tr > td.word");

var words = "";

topwords.forEach(function(topword){
    words = words + "\n"+topword.innerText;
});

console.log(words);
console.log(words.length);
