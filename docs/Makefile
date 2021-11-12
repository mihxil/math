

all: index.html

# sudo gem install asciidoctor asciidoctor-multipage

index.html: ../README.adoc Makefile algebras.svg
	asciidoctor $< -a linkcss -a stylesheet=index.css -o $@
	#asciidoctor --trace -r asciidoctor-multipage -b multipage_html5   -D pages $<

%.svg: %.dot
	dot  -Tsvg $< -o $@



%.html: %.adoc
	asciidoctor $<

clean:
	rm index.html algebras.svg
