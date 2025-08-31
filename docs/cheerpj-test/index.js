const settings = {
    version: 17
}
console.log("init cheerpj", settings);
await cheerpjInit(settings);

const pref = document.location.pathname.startsWith("/math") ?
    "/app/math/resources/jars/" :
    "/app/resources/jars/";
const version = "0.19-SNAPSHOT"
const cj = await cheerpjRunLibrary(`${pref}mihxil-math-${version}.jar:${pref}mihxil-math-parser-${version}.jar:${pref}mihxil-algebra-${version}.jar:${pref}mihxil-configuration-${version}.jar:${pref}mihxil-time-${version}.jar:${pref}original-mihxil-demo-${version}.jar:${pref}mihxil-functional-1.14.jar:${pref}big-math-2.3.2.jar`);


/* // it may be a hint that this does not work:
const Impl = await cj['org.meeuw.math.abstractalgebra.klein.KleinGroup']
const instance = await Impl.INSTANCE;

const sup = await instance.getSupportedOperators();
const a = await sup.toString()
*/
const Impl = await cj['org.meeuw.cheerpj.CheerpjTest']
const a = await Impl.getSupportedOperators();

const output = document.querySelector("output");



//const b = await instance.getB();
output.textContent = `
Operators of KleinGroup: ${a} (should be [OPERATION])
`
