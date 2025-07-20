
all: index.html positive-plane.mp4 diagonals-positive-plane.mp4 positive-3-plane.mp4

MVN=mvn
# sudo gem install asciidoctor asciidoctor-multipage

index.html: ../README-source.adoc ../README-source-docinfo.html Makefile algebras.svg
	asciidoctor $< -a linkcss -a stylesheet=index.css -a htmled=true -a docs=. -o $@
	#asciidoctor --trace -r asciidoctor-multipage -b multipage_html5   -D pages $<

%.svg: %.dot
	dot  -Tsvg $< -o $@

%.dot: %.dot.m4
	m4 $< > $@

%.html: %.adoc %-docinfo.html
	asciidoctor $<


%-3-plane.mp4: %-3-plane.data 3-frame.gp Makefile
	rm -f $@
	for i in {1..999} ; do (echo "\$$data << EOD" ; head -$$i $< ; echo EOD ; echo "set view ,$$(($$i / 3 % 360))"; cat 3-frame.gp) | gnuplot ; done | ffmpeg -f png_pipe -s:v 600x600 -i pipe: $@


%-plane.mp4: %-plane.data frame.gp Makefile
	rm -f $@
	for i in {1..999} ; do head -$$i $< | gnuplot frame.gp ; done | ffmpeg -f png_pipe -s:v 600x600 -i pipe: $@


clean:
	rm -f algebras.svg algebras.dot


algebras.dot.m4: ../mihxil-algebra/src/test/java/org/meeuw/test/math/abstractalgebra/DocumentationTest.java
	(cd ../mihxil-algebra ; if `which $(MVN) > /dev/null` ; then $(MVN) -DskipTests=false -Dtest=DocumentationTest test ;fi )

run:
	npx serve
