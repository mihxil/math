

all: index.html

# sudo gem install asciidoctor

index.html: README.adoc
	asciidoctor $< -a linkcss -a stylesheet=index.css -o $@

%.html: %.adoc
	asciidoctor $<
