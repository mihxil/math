

let cj = null;
async function setupCheerpj() {
    await cheerpjInit({version: 17});
    const pref = document.location.pathname.startsWith("/math") ? "/app/math/jars/" : "/app/jars/";
    const version = "0.19-SNAPSHOT"
    cj = await cheerpjRunLibrary(`${pref}mihxil-math-${version}.jar:${pref}mihxil-algebra-${version}.jar:${pref}mihxil-configuration-${version}.jar:${pref}mihxil-time-${version}.jar:${pref}mihxil-functional-1.14.jar`);
}


// tag::solver[]


async function setupSolver() {

    const form = document.querySelector('#solver');
    const button = form.querySelector('button');
    const buttonText = button.textContent;
    const textarea = form.querySelector('textarea');


    let Solver = null;
    form.onsubmit = async (e) => {
        e.preventDefault();
        button.disabled = true;
        if (cj === null) {
            button.textContent = "loading...";
            await setupCheerpj();
        }
        if (Solver == null) {
            button.textContent = "loading...";
            Solver = await cj.org.meeuw.math.abstractalgebra.rationalnumbers.Solver
            console.log(Solver);
        }
        const result = form.querySelector("#solver_result").value;
        const numbers = form.querySelector("#solver_numbers").value.split(" ");
        textarea.value = '';
        button.textContent = "executing..";
        try {
            const solverResult = await Solver.result(result, numbers);
            const stream = await solverResult.stream();
            const lines = await stream.toArray();
            for (let i = 0; i < lines.length; i++) {
                textarea.value += await lines[i].toString() + "\n";
            }
            const tries = await (await solverResult.tries()).get();
            const matches = await (await solverResult.matches()).get();
            textarea.value += `\nFound: ${matches}`;
            textarea.value += `\nTried: ${tries}`;
        } catch (error) {
            textarea.value += await error.toString();
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
    const textarea = form.querySelector('textarea');

    let DynamicDateTime = null;
    form.onsubmit = async (e) => {
        e.preventDefault();
        button.disabled = true;
        if (cj === null) {
            button.textContent = "loading...";
            await setupCheerpj();
        }
        if (DynamicDateTime == null) {
            button.textContent = "loading...";
            DynamicDateTime = await cj.org.meeuw.time.dateparser.DynamicDateTime
            console.log(DynamicDateTime);
        }
        textarea.value = '';
        button.textContent = "executing..";
        try {
            const parser = await new DynamicDateTime();
            const parseResult = await parser.parse(form.querySelector("#dynamicdate_toparse").value);
            textarea.value += await parseResult.toString();
        } catch (error) {
            textarea.value += await error.toString();
        }
        button.textContent = buttonText;
        button.disabled = false;
    };
}
setupDynamicDate();

//end::dynamicdate[]
