document.addEventListener('DOMContentLoaded', () => {
    const tocLinks = Array.from(document.querySelectorAll('#toc a'));

    function updateActive() {
        const hash = location.hash || '';
        tocLinks.forEach(a => {
            if (hash && a.getAttribute('href') === hash) {
                a.classList.add('active');
            } else {
                a.classList.remove('active');
            }
        });
    }

    window.addEventListener('hashchange', updateActive);
    updateActive();
});
