WORKDIR=${PWD}


# https://www.gnu.org/software/make/manual/make.html#Phony-Targets
.PHONY : clean usage pages prepare_pages

usage :
	@echo "Use \"make clean\" or \"make pages\""

prepare_pages:
	mkdir -p pages pages/html
	rsync -armR --include="*/" --include="*.adoc" --exclude="*" doc/ pages
	rsync -armR --include="*/" --include="*.puml" --exclude="*" doc/ pages
	rsync -armR --include="*/" --include="*.png" --exclude="*" doc/ pages
	cd pages && rsync -amR --include="*/" --include="*.png" --exclude="*" doc/ html
	cd pages && cd html/doc && rsync -amR --include="*/" --include="*.png" --exclude="*" . ..
	cd pages && cd html && rm -rf doc

pages : prepare_pages
	cd pages && asciidoctor -R doc -D html '**/*.adoc'

clean :
	-rm -r pages
	mvn clean

build: maven_build pages

maven_build:
	mvn install

all: clean build
