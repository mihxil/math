await cheerpjInit({
    version: 17
});

const pref = document.location.pathname.startsWith("/math") ?
    "/app/math/resources/jars/" :
    "/app/resources/jars/";
const version = "0.19-SNAPSHOT"
const cj = await cheerpjRunLibrary(`${pref}mihxil-math-${version}.jar:${pref}mihxil-math-parser-${version}.jar:${pref}mihxil-algebra-${version}.jar:${pref}mihxil-configuration-${version}.jar:${pref}mihxil-time-${version}.jar:${pref}original-mihxil-demo-${version}.jar:${pref}mihxil-functional-1.14.jar:${pref}big-math-2.3.2.jar`);

/* // it may be a hint that this does not work:
const Impl = await cj['org.meeuw.math.abstractalgebra.klein.KleinGroup']
const instance = await Impl.INSTANCE;
const sup = await instance.getSupportedOperators();
const result = await sup.toString()
*/
const Impl = await cj['org.meeuw.cheerpj.CheerpjTest']
const result = await Impl.getSupportedOperators();

const output = document.querySelector("output");

output.style.whiteSpace = "pre-wrap";
output.textContent = `
Operators of KleinGroup: ${result} (should be [OPERATION])

${result != '[OPERATION]' ? "FAILS" : 'success'}
`
