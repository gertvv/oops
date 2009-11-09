all: paper presentation

paper: paper.pdf

presentation: presentation.pdf

paper.pdf: paper.tex ref.bib abstract.tex examples.tex introduction.tex technical.tex discussion.tex features.tex rules.tex images/gui.png images/modelVis.png images/tableauVis.png
	./build.sh

presentation.pdf: presentation.tex
	rubber -d presentation.tex

clean:
	rm -f *.aux *.bbl *.blg *.log *.out *.spl *.pdf *.nav *.snm *.toc

.PHONY: all clean paper presentation
