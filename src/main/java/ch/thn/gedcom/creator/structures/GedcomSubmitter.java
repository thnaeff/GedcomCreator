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
package ch.thn.gedcom.creator.structures;

import ch.thn.gedcom.creator.GedcomCreatorError;
import ch.thn.gedcom.data.GedcomTree;
import ch.thn.gedcom.store.GedcomStore;


/**
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class GedcomSubmitter extends AbstractGedcomStructure {

	
	/**
	 * A new {@link AbstractGedcomStructure#SUBMITTER_RECORD} with the given ID
	 * 
	 * @param store
	 * @param id
	 */
	public GedcomSubmitter(GedcomStore store, String id) {
		super(store, SUBMITTER_RECORD, "SUBM");
		
		if (!setId(id)) {
			throw new GedcomCreatorError("Failed to create submitter record with ID " + 
					id + ". Id could not be set.");
		}
		
	}
	
	/**
	 * Creates a new individual using the given gedcom head node ({@link GedcomTree}) 
	 * which has to be a {@link AbstractGedcomStructure#SUBMITTER_RECORD} structure.
	 * 
	 * @param store
	 * @param gedcomHeadNode
	 */
	public GedcomSubmitter(GedcomStore store, GedcomTree gedcomHeadNode) {
		super(store, SUBMITTER_RECORD, gedcomHeadNode, "SUBM");
	}
	
	@Override
	public String getStructureName() {
		return AbstractGedcomStructure.SUBMITTER_RECORD;
	}
	
	/**
	 * 
	 * 
	 * @param id
	 * @return
	 * @throws GedcomCreatorError
	 */
	public boolean setId(String id) {
		if (id == null || id.length() == 0) {
			throw new GedcomCreatorError("Setting an empty ID is not allowed");
		}
		
		return createAndSet(new GedcomXRef(false, id));
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
		return createAndSet(new GedcomValue(false, name, "NAME"));
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getSubmitterName() {
		return getValue("NAME");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeSubmitterName() {
		return remove("NAME");
	}

}
