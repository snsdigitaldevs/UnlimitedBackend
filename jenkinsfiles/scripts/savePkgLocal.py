#!/usr/bin/env python
# -*- encoding:utf-8 -*-

import os
import sys
import subprocess

# reserve only the latest jar package for save space
ReservePkgNum = 30

# save build package in local
def save(SourcePath,DestPath):
    subprocess.call("cp %s %s"%(SourcePath,DestPath),shell=True)

# delete expire package
def delExpirePkgs(SaveDir):

    jarslist = os.listdir(SaveDir)
    jarslist.sort(key=lambda x: int(os.path.splitext(x)[0].split("_")[1]),reverse=True)
    
    if len(jarslist) > ReservePkgNum:

    	NeedDelPkgs = jarslist[ReservePkgNum::]

    	for i in NeedDelPkgs:

    		PkgPath = SaveDir + os.sep + i

    		subprocess.call("rm -f %s"%PkgPath,shell=True)


# main
OriPkgPath = sys.argv[1]
SaveDir = sys.argv[2]
NewPkgName = sys.argv[3]  
NewPkgPath = SaveDir + os.sep + NewPkgName

save(OriPkgPath,NewPkgPath)
delExpirePkgs(SaveDir)



