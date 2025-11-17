/* DO NOT EDIT. Copied from ../mihxil-demo/js/resources/ */
import {BaseClass} from "./base.js";

document.addEventListener("DOMContentLoaded", async function() {
    document.querySelectorAll(".demo button").forEach(button => {
        button.setAttribute('data-original-text', button.textContent);
        button.textContent = "waiting";
        button.disabled = true;
    });

    const isDemo = document.querySelector('meta[name="demo"]')?.content;
    if (isDemo) {
        console.log("demo page");
        const cjj = await BaseClass.getCheerpj();
       /* const ArrayList = await cjj['java.util.ArrayList'];
        console.log("class", ArrayList);
        const list = await (new ArrayList());
        console.log("list", list);
        await list.add("foo");
        await list.add("bar");
        console.log("list", await list.toString());
        const stream = await list.stream();
        const i = await stream.iterator();
        while (await i.hasNext()) {
            console.log("list item", await i.next());
        }
        console.log(list);*/
    }
});
