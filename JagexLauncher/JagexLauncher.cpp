/*
 * Copyright 2010 Jagex Limited.
 *
 * This file is part of JagexLauncher.
 * 
 * JagexLauncher is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * JagexLauncher is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, see <http://www.gnu.org/licenses/>
 *
 */

#include "stdafx.h"
#include <iostream>
using std::cout;
using std::endl;
#define MAX_LINE_LENGTH 65536

#ifndef UNICODE
#error "Only unicode mode currently supported"
#endif

int mainprogram(char *game, char *prmfile, char *uri)
{
	FILE *f;
	char **paramlines;
	int l_paramlines=0;
	if (fopen_s(&f, prmfile, "r") != 0) {
		MessageBox(NULL, TEXT("Unable to load parameter file. Please reinstall the program."), TEXT("Error"), MB_OK);
		return 0;
	} else {
		char line[MAX_LINE_LENGTH];
		while (fgets(line, MAX_LINE_LENGTH, f)!=NULL) l_paramlines++;
		paramlines=new char *[l_paramlines];
		fseek(f, 0, SEEK_SET);
		int i=0;
		while (fgets(line, MAX_LINE_LENGTH, f)!=NULL) {
			int len=strlen(line);
			for (int j=len-1; j>=0; j--) {
				if (line[j]!='\n' && line[j]!='\r') break;
				line[j]=0;
			}
			paramlines[i]=new char[len+1];
			strcpy_s(paramlines[i], len+1, line);
			i++;
		}

	}
	if (l_paramlines<1) {
		MessageBox(NULL, TEXT("Empty parameter file. Please reinstall the program."), TEXT("Error"), MB_OK);
		return 0;
	}

	JavaVM *jvm;
	JNIEnv *env;
	LoadLibrary(TEXT("jvm.dll"));
	JavaVMInitArgs vm_args;
	vm_args.version=JNI_VERSION_1_6;
	if (uri != NULL)
	{
		vm_args.nOptions=l_paramlines;
	}
	else 
	{
		vm_args.nOptions=l_paramlines-1;
	}
	vm_args.options=new JavaVMOption[vm_args.nOptions];
	int index=0;
	char* buffer;
	if (uri != NULL)
	{
		int bufferLen=512;
		buffer = new char[bufferLen];
		strcpy_s(buffer, bufferLen, "-Dcom.jagex.configuri=");
		strcat_s(buffer, bufferLen, uri);
		vm_args.options[index++].optionString = buffer;
	}
	for (int i=0; i<l_paramlines-1; i++) vm_args.options[index++].optionString=paramlines[i];
	vm_args.ignoreUnrecognized=false;
	jint result=JNI_CreateJavaVM(&jvm, reinterpret_cast<void **>(&env), &vm_args);
	if (result!=JNI_OK) return 0;
	jclass cls=env->FindClass(paramlines[l_paramlines-1]);
	if (cls!=NULL) {
		jmethodID mid=env->GetStaticMethodID(cls, "main", "([Ljava/lang/String;)V");
		jobjectArray args=env->NewObjectArray(1, env->FindClass("java/lang/String"), NULL);
		env->SetObjectArrayElement(args,0,env->NewStringUTF(game));
		env->CallStaticVoidMethod(cls, mid, args);
	}

	jvm->DestroyJavaVM();
	return 0;
}

char* convertWideString(TCHAR *input)
{
	std::wstring wide(input);
	int length = WideCharToMultiByte(CP_ACP, 0, &wide[0], (int)wide.size(), NULL, 0, NULL, NULL);
	char* result=new char[length+1];
	WideCharToMultiByte(CP_ACP, 0, &wide[0], (int)wide.size(), result, length, NULL, NULL);
	result[length]=0;
	return result;
}

int runMain(int argc, TCHAR **argv)
{
	if (argc<2) return 0;
	std::wstring path(argv[0]);
	size_t endDir = path.find_last_of(L"/\\");
	std::wstring dir = path.substr(0, endDir);
	SetCurrentDirectoryW(dir.c_str());

	char *game = convertWideString(argv[1]);
	char *uri = NULL;
	char *prmFile = NULL;
	if (argc >= 3)
	{
		prmFile = convertWideString(argv[2]);

		if (argc >= 4)
		{
			uri = convertWideString(argv[3]);
		}
	}
	if (prmFile == NULL)
	{
		prmFile = new char[512];
		sprintf_s(prmFile, 512, "../%s/%s.prm", game, game);
	}
	int ret=mainprogram(game, prmFile, uri);
	return ret;
}

#ifdef _CONSOLE
int _tmain(int argc, TCHAR **argv)
{
	return runMain(argc, argv);
}
#else
int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPSTR lpCmdLine, int nShowCmd)
{
	LPWSTR* szArgList;
	int argCount;
	szArgList = CommandLineToArgvW(GetCommandLineW(), &argCount);
	return runMain(argCount, szArgList);
}
#endif