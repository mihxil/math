document.addEventListener("DOMContentLoaded", async function() {
    document.querySelectorAll(".demo button").forEach(button => {
        button.setAttribute('data-original-text', button.textContent);
        button.textContent = "waiting";
        button.disabled = true;
    });
});
