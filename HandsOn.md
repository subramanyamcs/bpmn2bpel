#First source code release

# BPMN2BPEL plugin, release 1 #

Finally, the source code is now available. I received several requests for the code so I decided to release it "as-is". I will try to improve it later ...

To start working with the code you must setup you Eclipse following the procedure described in the "instalation.pdf" file in the **Downloads** section. Stragely, classes on the BPEL plugin get all prefixed with a package "bin" (e.g. org.eclipse.model.Process becomes bin.org.eclipse.model.Process). My workaround is to checkout the BPEL Plugin code at the CVS repository (see http://www.eclipse.org/bpel/install.php) and then import every subproject in the org.eclipse.bpel/plugins to your workspace. After that, you can import the BPMN2BPEL plugin code to your workspace and start working !

Roughly speaking, the translation is done in three steps:

1. The BPMN Model (a single Pool) is partitioned into Single Entry-Single Exit (SESE) regions. Such regions are organized in a tree (called the Process/Program Structure Tree). This step is performed by classes in package `translation.analysis.sese`. Note that this package is an adaptation to a open source implementation to Johnson's algoritms (which was available at http://flex-compiler.csail.mit.edu)

2. Following a postorder traversal on the tree, every SESE region is analyzed to gather control flow information. With this information, the subgraph in the region is factored out to a BPMN subprocess, which is annotated with the pattern characterizing the region (e.g. Structured block, Structured cycle, etc.) By the end of this step, the BPMN model has been restructured to reflect the inherent structure (to help translation). As described in the "getting started.pdf" document, you can save the transformed BPMN Model. All this step is done with classes in package `translation.analysis.cfa` and `translation.Rewritter` class.

3. The resulting BPMN model is then translated to BPEL. This is done by `bpelfactory.Translator` class.
