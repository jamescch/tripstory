/*
 * Copyright 2013, Edmodo, Inc. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License.
 * You may obtain a copy of the License in the LICENSE file, or at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" 
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language 
 * governing permissions and limitations under the License. 
 */

package com.triwalks.Cropper.cropwindow.edge;

import com.triwalks.Cropper.cropwindow.edge.*;

/**
 * Simple class to hold a pair of Edges.
 */
public class EdgePair {

    // Member Variables ////////////////////////////////////////////////////////

    public com.triwalks.Cropper.cropwindow.edge.Edge primary;
    public com.triwalks.Cropper.cropwindow.edge.Edge secondary;

    // Constructor /////////////////////////////////////////////////////////////

    public EdgePair(com.triwalks.Cropper.cropwindow.edge.Edge edge1, com.triwalks.Cropper.cropwindow.edge.Edge edge2) {
        primary = edge1;
        secondary = edge2;
    }
}
