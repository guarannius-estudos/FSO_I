function searchGames() {
    const query = document.getElementById('search').value;
    
    if (query.trim() !== "") {
        window.location.href = `/game/search/${encodeURIComponent(query)}`;
    } else {
        alert('Por favor, insira um termo de pesquisa');
    }
}

document.getElementById('search-btn').addEventListener('click', searchGames);

document.getElementById('search').addEventListener('keydown', function(event) {
    if (event.key === 'Enter') {
        searchGames();
    }
});
