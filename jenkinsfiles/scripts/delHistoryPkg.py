#!/usr/bin/env python
# -*- encoding:utf-8 -*-

import os
import sys
import subprocess
import pipes
import shlex

# reserve only the latest jar package for save space
ReservePkgNum = 30

# delete expire package
def delExpirePkgs(SaveDir):

    jarslist = os.listdir(SaveDir)
    jarslist.sort(key=lambda x: os.path.getctime(os.path.join(SaveDir, x)),reverse=True)

    if len(jarslist) > ReservePkgNum:

        NeedDelPkgs = jarslist[ReservePkgNum::]

        for i in NeedDelPkgs:
            PkgPath = SaveDir + os.sep + i
            args = shlex.split("rm -f {}".format(pipes.quote(PkgPath)))
            subprocess.Popen(args)

SaveDir = sys.argv[1]
delExpirePkgs(SaveDir)
