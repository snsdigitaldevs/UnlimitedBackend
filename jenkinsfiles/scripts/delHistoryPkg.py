#!/usr/bin/env python
# -*- encoding:utf-8 -*-

import os
import sys
import subprocess

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

    		subprocess.call("rm -f %s"%PkgPath,shell=True)

delExpirePkgs(SaveDir)



