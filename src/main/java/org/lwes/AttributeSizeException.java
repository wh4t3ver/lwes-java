/*======================================================================*
 * Copyright (c) 2010, Frank Maritato All rights reserved.              *
 *                                                                      *
 * Licensed under the New BSD License (the "License"); you may not use  *
 * this file except in compliance with the License.  Unless required    *
 * by applicable law or agreed to in writing, software distributed      *
 * under the License is distributed on an "AS IS" BASIS, WITHOUT        *
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.     *
 * See the License for the specific language governing permissions and  *
 * limitations under the License. See accompanying LICENSE file.        *
 *======================================================================*/

package org.lwes;
/**
 * @author fmaritato
 */

public class AttributeSizeException extends EventSystemException {

    public AttributeSizeException(Throwable e) {
        super(e);
    }

    public AttributeSizeException(String s) {
        super(s);
    }

    public AttributeSizeException(String s, Throwable e) {
        super(s, e);
    }

    public AttributeSizeException(String attribute, int size, int expectedSize) {
        super("Attribute "+attribute+" size is incorrect. Expected "+expectedSize+" but was "+size);
    }

    public AttributeSizeException(String attribute, int size, int expectedSize, Throwable e) {
        super("Attribute "+attribute+" size is incorrect. Expected "+expectedSize+" but was "+size, e);
    }
}
