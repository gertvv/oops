Introduction
==============================================================================

This is OOPS, the Object Oriented Prover for S5n. OOPS was created by [Elske
van der Vaart](http://www.ai.rug.nl/~elskevdv/) and [Gert van
Valkenhoef](http://www.gertvv.nl/) in the context of a [Multi-Agent
Systems](http://www.ai.rug.nl/mas/) project.

OOPS makes use of a tableau proof method in order to prove or disprove
formulas in the S5n modal logic. More information can be found on the
[OOPS wiki on GitHub](http://wiki.github.com/gertvv/oops).

To encourage future work on OOPS, the source code is available under the GNU
General Public License (GNU GPL). It is our hope that OOPS will be tested,
proven correct, extended or otherwise enhanced in the context of future
student projects.


License
==============================================================================

    OOPS - Object Oriented Prover for S5n
    Copyright (C) 2007-2009 Elske van der Vaart and Gert van Valkenhoef
		  2012 Gert van Valkenhoef, Wouter Reckman, Lourens Elzinga,
                       and Rik Timmers.
                  2014 Gert van Valkenhoef

    This program is free software; you can redistribute it and/or modify it
    under the terms of the GNU General Public License version 2 as published
    by the Free Software Foundation.
	
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

    See the 'LICENSE' file in the source distribution for more information.


Running OOPS
==============================================================================

To run OOPS from the provided ZIP file, you need Java version 1.5 or higher.

Example:

	$ java -jar oops.jar

Or simply double-click on oops.jar.


Compiling from source
==============================================================================

Requirements
------------------------------------------------------------------------------

In order to build the sources in this directory you need:

  - [Sun Java](http://java.sun.com/) 1.5 or higher (or any compatible
implementation)
  - [Maven2](http://maven.apache.org/)

Any additional dependencies will be downloaded automatically via Maven.

Getting the latest version from GitHub
------------------------------------------------------------------------------

You will need to have [Git](http://git-scm.com/) installed. Run the following
command:

    $ git clone git://github.com/gertvv/oops.git

Alternatively, go to http://github.com/gertvv/oops and see the downloads
section, where you can retrieve source for any of the released versions of
OOPS.

Building the JAR
------------------------------------------------------------------------------

Do the following:

	$ mvn package

This will create application/target/oops-$VERSION-jar-with-dependencies.jar,
which you can use to run OOPS.

Running the test suite
------------------------------------------------------------------------------

The maven build automatically runs all provided unit tests (under src/test).
Test reports are generated under target/surefire-reports.

Developing in Eclipse
------------------------------------------------------------------------------

Use the M2 Eclipse plugin to import OOPS as an existing maven project.
