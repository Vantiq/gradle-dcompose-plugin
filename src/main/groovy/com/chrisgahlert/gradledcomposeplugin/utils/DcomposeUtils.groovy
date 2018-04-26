/*
 * Copyright 2016 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chrisgahlert.gradledcomposeplugin.utils

import com.chrisgahlert.gradledcomposeplugin.extension.Service
import org.gradle.api.Project

import java.security.MessageDigest

class DcomposeUtils {
    static String sha1Hash(String source) {
        def sha1 = MessageDigest.getInstance("SHA1")
        def digest = sha1.digest(source.bytes)
        def hash = new BigInteger(1, digest).toString(16)
        hash
    }

    static File getDefaultBaseDir(Service service, Project project) {
        new File(project.buildDir, "dcompose-build/$service.name")
    }
}
