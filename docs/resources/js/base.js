/* DO NOT EDIT. Copied from ../mihxil-demo/js/resources/ */


export class BaseClass {
    static cj = null;
    static cjInit = null;

    constructor(formSelector, ...classes) {

        const [first, ...rest] = classes;
        this.className = first;
        this.classNames = rest;
        this.form = document.querySelector(formSelector);

        this.output = this.form.querySelector('output');
        this.button = this.form.querySelector('button');
        this.ready = false;
    }


    static async getCheerpj() {

        if (BaseClass.cjInit === null) {

            BaseClass.cjInit = "locked";
            const properties = [
                `user.timezone=${Intl.DateTimeFormat().resolvedOptions().timeZone}`];
            function showPreloadProgress(preloadDone, preloadTotal) {
                const percentage = (preloadDone * 100) / preloadTotal;
                //console.log("Percentage loaded " + (preloadDone * 100) / preloadTotal);
                // Update all loading buttons
                console.log(percentage);

                document.querySelectorAll('button.cheerpjprogress.loading').forEach(btn => {
                    console.log(btn, percentage);
                    btn.style.setProperty('--progress', `${percentage}%`);
                });
            }

            const settings = {
                version: 17,
                enableDebug: false,
                javaProperties: properties,
                preloadResources: JSON.parse('{"/lt/17/conf/logging.properties":[0,131072],"/lt/17/lib/modules":[0,131072,1179648,3801088,3932160,5242880,5373952,5898240,6029312,6291456,6422528,6553600,6684672,7077888,7864320,8126464,9568256,9699328,9830400,10092544,19005440,19136512,27787264,27918336,37486592,37617664,38010880,38141952],"/lt/17/conf/security/java.security":[0,131072],"/lt/17/jre/lib/cheerpj-jsobject.jar":[0,131072],"/lt/17/jre/lib/cheerpj-awt.jar":[0,131072],"/lt/17/jre/lib/cheerpj-handlers.jar":[0,131072],"/lt/etc/users":[0,131072],"/lt/etc/localtime":[],"/lt/17/lib/tzdb.dat":[0,131072]}'),
                preloadProgress: showPreloadProgress
            }
            console.log("init cheerpj", settings);
            BaseClass.cjInit = cheerpjInit(settings);
        }
        await BaseClass.cjInit;
        if (BaseClass.cj === null) {
            const version = "0.19-SNAPSHOT";
            const pref = document.location.pathname.startsWith("/math") ?
                "/app/math/resources/jars/" :
                "/app/resources/jars/";
            const resources = `${pref}mihxil-math-${version}.jar:${pref}mihxil-math-parser-${version}.jar:${pref}mihxil-algebra-${version}.jar:${pref}mihxil-configuration-${version}.jar:${pref}mihxil-time-${version}.jar:${pref}original-mihxil-demo-${version}.jar:${pref}mihxil-functional-1.15.jar:${pref}big-math-2.3.2.jar`;
            BaseClass.cj = cheerpjRunLibrary(resources);
        }

        return await BaseClass.cj;
    }

    static async loadClassesForForm(className, classNames) {
        const cjj = await BaseClass.getCheerpj();

        await (await cjj['org.meeuw.math.demo.DemoUtils']).setupLogging('INFO');
        console.log(cjj, "setting up", className, classNames);
        for (const c of classNames) {
            await cjj[c]
        }
        const clazz = await cjj[className];
        if (!clazz) {
            throw new Error(cjj, "Could not load ", className, classNames);
        }
        return clazz;
    }

    static async awaitedArray(array) {
        const awaited = [];
        const a = await array;
        for (let j = 0; j < a.length; j++) {
            awaited[j] = await a[j];
        }
        return awaited;
    }

    /**
     * Just puts the normal text back on the submit button and enables it.
     */
    readyToGo() {
        console.log("Ready", this.form);
        this.button.textContent = this.button.getAttribute("data-original-text");
        this.button.disabled = false;
        this.form.classList.remove('disabled');
        this.button.classList.remove('loading');
        this.ready = true;
    }

    async setupForm() {

        if (!this.Class) {
            this.button.disabled = true;
            this.form.classList.add('disabled'); // To gray out
            this.button.classList.add('loading');
            this.button.textContent = "loading...";
            this.Class = "locked";
            this.Class = await BaseClass.loadClassesForForm(this.className, this.classNames);
            console.log(this.Class.prototype, "set up");
        } else if (this.Class === "locked") {
            while (this.Class === "locked") {
                await new Promise(resolve => setTimeout(resolve, 50)); // check every 50ms
            }
        } else {
            //console.log(this.Class, "already set up");
        }
        await this.setupDataLists();
        await this.setupValidation();
    }

    async setupDataLists() {
        // if someone clicks on a datalist option, fill the value in the
        // corresponding input
        if (! this.form.getAttribute('data-data_lists-setup')) {
            this.form.setAttribute('data-data_lists-setup',  true);
            const data_lists = this.form.querySelectorAll("datalist");
            for (const dl of data_lists) {
                dl.addEventListener('click', async (e) => {
                    const datalist = e.target.closest('datalist').id;
                    const optionValue = e.target.value;
                    if (optionValue) {
                        const datalists = document.querySelectorAll(`*[list="${datalist}"]`);
                        for (const e of datalists) {
                            if (e.value !== optionValue) {
                                e.value = optionValue;

                                const event = new CustomEvent('exampleFilled', {
                                    detail: {
                                        optionValue: optionValue
                                    }
                                });
                                this.form.dispatchEvent(event);
                                this.output.value = '';
                                const inputEvent = new InputEvent('input', {bubbles: true});

                                e.dispatchEvent(inputEvent)
                            }
                        }
                    }
                });
            }
        }
    }

    async onSubmit(Class) {
        console.log("No override, this is abstract");

    }

    async onInView() {
        console.log("in view", await this.className);
        this.button.textContent = "loading...";
        await this.setupForm();
        await this.setupSubmit()
    }

    async handleSubmit() {
        try {
            //console.log("submitting for",  this.Class.prototype);
            this.output.value = '';
            this.button.textContent = "executing..";
            await this.onSubmit(this.Class);
        } catch (e) {
            console.log("exception", e, e.stack);
            this.output.value = e.stack ? e.stack : await e.toString();
        } finally {
            console.log("submit ready");
            await this.readyToGo();
        }
    }

    async setupSubmit() {
        this.form.onsubmit = async (e) => {
            e.preventDefault();
            await this.handleSubmit();
        };
    }

    /**
     *
     */
    async setup() {
        this.button.disabled = true;
        console.log("created", this.button);
        // enforce rendering
        await new Promise(resolve => setTimeout(resolve, 0));

        const observer = new IntersectionObserver(async (entries) => {
            for (const entry of entries) {
                if (entry.isIntersecting) {
                    console.log("intersecting", await entry.target, "setup form");
                    await this.onInView(this.Class);
                    //console.log("readyToGo", this.Class.prototype);
                    await this.readyToGo();
                    await observer.disconnect();
                }
            }
        }, {threshold: 0.1});
        await observer.observe(this.form);
    }


    async setupValidation() {
        if (!this.form.getAttribute('data-validation-setup')) {
            this.form.setAttribute('data-validation-setup', true);
            const inputs = this.form.querySelectorAll("input[pattern]");
            for (const input of inputs) {
                input.addEventListener("input", function (e) {
                    const input = e.target;
                    const pattern = new RegExp(input.getAttribute("pattern"));

                    if (input.value && !pattern.test(input.value)) {
                        const errorMessage = input.getAttribute("data-error-message") || "Invalid input";

                        input.setCustomValidity(errorMessage);
                        // Optional: add visual feedback
                        input.classList.add("invalid");
                    } else {
                        input.setCustomValidity("");
                        input.classList.remove("invalid");
                    }
                });
            }
            const inputs2 = this.form.querySelectorAll("input[data-parser]");
            for (const input of inputs2) {
                input.addEventListener("input", async function (e) {
                    const input = e.target;
                    const parser = input.getAttribute("data-parser");
                    const model = input['model'];
                    const error = await model[parser](input.value);
                    if (error !== null) {
                        const errorMessage = input.getAttribute("data-error-message") || "Invalid input";
                        input.setCustomValidity(errorMessage);
                        // Optional: add visual feedback
                        input.classList.add("invalid");
                        input.reportValidity(); // Show the message immediately
                    } else {
                        input.setCustomValidity("");
                        input.classList.remove("invalid");
                    }
                });
            }
        }
    }
}


