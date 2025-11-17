import { BaseClass } from "./base.js";

document.addEventListener('DOMContentLoaded', async () => {
    const tocLinks = Array.from(document.querySelectorAll('#toc a'));

    function updateActive() {
        const hash = location.hash || '';
        let matched = false;
        tocLinks.forEach(a => {
            if (hash && a.getAttribute('href') === hash) {
                a.classList.add('active');
                matched = true;
            } else {
                a.classList.remove('active');
            }
        });
        if (!matched) {
            tocLinks[0].classList.add('active');
            location.hash = tocLinks[0].getAttribute('href');
        }
    }

    window.addEventListener('hashchange', updateActive);

    updateActive();
     const cjj = await BaseClass.getCheerpj();
});
