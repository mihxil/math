const form = document.querySelector('form');
const button = document.getElementById('submit');

form.onsubmit = async (e) => {
  e.preventDefault();
};

await cheerpjInit({version: 17});

const pref = document.location.pathname.startsWith("/math") ? "/app/math/jars/": "/app/jars/";


const version= "0.19-SNAPSHOT"
const cj = await cheerpjRunLibrary(`${pref}mihxil-math-${version}.jar:${pref}mihxil-algebra-${version}.jar:${pref}mihxil-configuration-${version}.jar:${pref}mihxil-functional-1.14.jar`);

const Solver = await cj.org.meeuw.math.abstractalgebra.rationalnumbers.Solver


const textarea = document.querySelector('textarea');

button.textContent = "go!";
button.disabled = false;
form.onsubmit =  async (e) => {
	e.preventDefault();
	textarea.value = '';
	button.textContent = "executing..";
	button.disabled = true;
	const result = document.querySelector("#result").value;
	const numbers = document.querySelector("#numbers").value.split(" ");

	try {
		const stream = await Solver.result(result, numbers);
		const lines = await stream.toArray()
		for (let i = 0; i < lines.length; i++) {
			textarea.value += await lines[i].toString() + "\n";
		}
	} catch (error) {
		textarea.value += await error.toString();
	}
	button.textContent = "go!";
	button.disabled = false;

}


