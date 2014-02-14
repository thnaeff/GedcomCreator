/**
 *    Copyright 2013 Thomas Naeff (github.com/thnaeff)
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
 * 
 */
package ch.thn.gedcom.creator;

import ch.thn.gedcom.data.GedcomNode;
import ch.thn.gedcom.store.GedcomStore;


/**
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class GedcomCreatorSubmitter extends GedcomCreatorStructure {

	
	/**
	 * A SUBMITTER_RECORD
	 * 
	 * @param store
	 * @param id
	 */
	public GedcomCreatorSubmitter(GedcomStore store, String id) {
		super(store, "SUBMITTER_RECORD", "SUBM");
		
		if (!setId(id)) {
			throw new GedcomCreatorError("Failed to create submitter record with ID " + 
					id + ". Id could not be set.");
		}
		
	}
	
	/**
	 * 
	 * 
	 * @param store
	 * @param node
	 */
	public GedcomCreatorSubmitter(GedcomStore store, GedcomNode node) {
		super(store, "SUBMITTER_RECORD", node);
	}
	
	/**
	 * 
	 * 
	 * @param id
	 * @return
	 */
	public boolean setId(String id) {
		return apply(new GedcomXRef(false, id));
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getId() {
		return getXRef();
	}
	
	
	
	/**
	 * 
	 * 
	 * @param name
	 * @return
	 */
	public boolean setSubmitterName(String name) {
		return apply(new GedcomValue(false, name, "NAME"));
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getSubmitterName() {
		return getValue("NAME");
	}

}
