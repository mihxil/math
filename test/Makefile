
URL:=mihxil/math-test:latest

mvn:
	(cd ../mihxil-configuration/ ; mvn  install ) ; (cd ../mihxil-math/ ; mvn install ) ; mvn

docker: Dockerfile
	docker build -t $(URL) .

runro:
	docker run -v $(HOME):/root:ro -it  $(URL) prefs

run:
	docker run -v $(HOME):/root -it  $(URL)

runprefs:
	docker run -v $(HOME):/root -it  $(URL) prefs

explore:
	docker run  -$(HOME):/root:ro -it --entrypoint /bin/bash $(URL)
