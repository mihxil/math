const form = document.querySelector('form');
const button = document.getElementById('submit');

form.onsubmit = async (e) => {
  e.preventDefault();
  // your code here
};
await cheerpjInit({version: 17});
const pref = "/app/math/jars/"
const version= "0.19-SNAPSHOT"
const cj = await cheerpjRunLibrary(`${pref}mihxil-math-${version}.jar:${pref}mihxil-algebra-${version}.jar:${pref}mihxil-configuration-${version}.jar:${pref}mihxil-functional-1.13.jar`);

const Solver = await cj.org.meeuw.math.abstractalgebra.rationalnumbers.Solver


const textarea = document.querySelector('textarea');
console.log("ready");
button.disabled = false;
form.onsubmit =  async (e) => {
	console.log("preventing default");
	e.preventDefault();
	textarea.value = '';
	button.disabled = true;
	const result = document.querySelector("#result").value;
	const numbers = document.querySelector("#numbers").value.split(" ");

	const stream = await Solver.result(result, numbers);
  const lines = await stream.toArray()
	for (let i = 0; i < lines.length; i++) {
		textarea.value += await lines[i].toString();
	}
	button.disabled = false;

};


