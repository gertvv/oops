#!/bin/bash

rm paper.pdf

STYLE="../styles/entcs/"
export TEXINPUTS=".:$STYLE:"
export BSTINPUTS=".:$STYLE:"
rubber -d paper.tex
