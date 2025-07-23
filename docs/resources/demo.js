

let cj = null;
async function setupCheerpj() {
    const properties=  [
        `user.timezone=${Intl.DateTimeFormat().resolvedOptions().timeZone}`
    ]
    await cheerpjInit({
        version: 17,
        javaProperties: properties
    });
    const pref = document.location.pathname.startsWith("/math") ? "/app/math/resources/jars/" : "/app/resources/jars/";
    const version = "0.19-SNAPSHOT"
    cj = await cheerpjRunLibrary(`${pref}mihxil-math-${version}.jar:${pref}mihxil-algebra-${version}.jar:${pref}mihxil-configuration-${version}.jar:${pref}mihxil-time-${version}.jar:${pref}original-mihxil-demo-${version}.jar:${pref}mihxil-functional-1.14.jar`);
}

async function setupFormWithClass(button, clazz) {
    button.disabled = true;
    button.textContent = "loading...";
    if (cj === null) {
        await setupCheerpj();
    }
    return await cj[clazz]
}

// tag::solver[]


async function setupSolver() {

    const form = document.querySelector('#solver');
    const button = form.querySelector('button');
    const buttonText = button.textContent;
    const output = form.querySelector('output');

    let Solver = null;
    form.onsubmit = async (e) => {
        e.preventDefault();
        Solver = await setupFormWithClass(button, 'org.meeuw.math.test.Solver');
        await cj.org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers

        const result = form.querySelector("#solver_result").value;
        const numbers = form.querySelector("#solver_numbers").value.split(" ");
        output.value = '';
        button.textContent = "executing..";
        try {
            const solverResult = await Solver.result(result, numbers);
            const stream = await solverResult.stream();
            const lines = await stream.toArray();
            for (let i = 0; i < lines.length; i++) {
                output.value += await lines[i].toString() + "\n";
            }
            const tries = await (await solverResult.tries()).get();
            const matches = await (await solverResult.matches()).get();
            output.value += `\nFound: ${matches}`;
            output.value += `\nTried: ${tries}`;
        } catch (error) {
            output.value += await error.toString();
        }
        button.textContent = buttonText;
        button.disabled = false;
    };
}
setupSolver();

//end::solver[]




// tag::dynamicdate[]


async function setupDynamicDate() {

    const form = document.querySelector('#dynamicdate');
    const button = form.querySelector('button');
    const buttonText = button.textContent;
    const output = form.querySelector('output');

    let DynamicDateTime = null;
    form.onsubmit = async (e) => {
        e.preventDefault();
        DynamicDateTime = await setupFormWithClass(button, 'org.meeuw.time.dateparser.DynamicDateTime');
        button.textContent = "executing..";
        try {
            const parser = await new DynamicDateTime();
            const parseResult = await parser.applyWithException(form.querySelector("#dynamicdate_toparse").value);
            output.value = await parseResult.toString();
        } catch (error) {
            output.value = await error.toString();
        }
        button.textContent = buttonText;
        button.disabled = false;
    };
}
setupDynamicDate();

//end::dynamicdate[]
