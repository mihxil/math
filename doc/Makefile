

all: index.html

# sudo gem install asciidoctor asciidoctor-multipage

index.html: ../README.adoc
	asciidoctor $< -a linkcss -a stylesheet=index.css -o $@
	#asciidoctor --trace -r asciidoctor-multipage -b multipage_html5   -D pages $<

%.html: %.adoc
	asciidoctor $<
