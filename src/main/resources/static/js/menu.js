function toggleMenu() {
    var menu = document.getElementById("menuLinks");
    if (menu.style.display === "block") {
        menu.style.display = "none";
    } else {
        menu.style.display = "block";
    }
}

window.onclick = function(event) {
    if (!event.target.matches('.menu-button')) {
        var menu = document.getElementById("menuLinks");
        if (menu && menu.style.display === "block") {
            menu.style.display = "none";
        }
    }
}
