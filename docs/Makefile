

all: index.html

# sudo gem install asciidoctor asciidoctor-multipage

index.html: ../README.adoc Makefile algebras.svg
	asciidoctor $< -a linkcss -a stylesheet=index.css -a docs=. -o $@
	#asciidoctor --trace -r asciidoctor-multipage -b multipage_html5   -D pages $<

%.svg: %.dot
	dot  -Tsvg $< -o $@

%.dot: %.dot.m4
	m4 $< > $@

%.html: %.adoc
	asciidoctor $<

clean:
	rm -f algebras.svg algebras.dot
