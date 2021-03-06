/*
 * Copyright 2013 Jon Iles
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rtfparserkit.parser;

import java.io.IOException;

/**
 * Standard interface implemented by an RTF parser.
 */
public interface IRtfParser
{
   /**
    * At the point the parser is constructed, a listener will have been
    * supplied by the caller. When this method is called, the parser reads
    * RTF data from the source and calls the listener with details
    * of the content.
    */
   void parse(IRtfSource source, IRtfListener listener) throws IOException;
}
