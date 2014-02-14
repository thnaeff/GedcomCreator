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
public class GedcomCreatorEOF extends GedcomCreatorStructure {

	
	/**
	 * A END_OF_FILE
	 * 
	 * @param store
	 */
	public GedcomCreatorEOF(GedcomStore store) {
		super(store, "END_OF_FILE");
		
		apply(new GedcomDataEmpty(false, "TRLR"));
	}
	
	/**
	 * 
	 * 
	 * @param store
	 * @param node
	 */
	public GedcomCreatorEOF(GedcomStore store, GedcomNode node) {
		super(store, "END_OF_FILE", node, "TRLR");
	}
	
	/**
	 * Not available for END_OF_FILE
	 * 
	 */
	@Override
	public boolean setChangeDate(String changeDate, String changeTime) {
		throw new UnsupportedOperationException("No change date available in END_OF_FILE.");
	}
	
	/**
	 * Not available for END_OF_FILE
	 * 
	 */
	@Override
	public String getChangeDate() {
		throw new UnsupportedOperationException("No change date available in END_OF_FILE.");
	}
	
	/**
	 * Not available for END_OF_FILE
	 * 
	 */
	@Override
	public boolean setNote(int index, String note) {
		throw new UnsupportedOperationException("No notes available in END_OF_FILE.");
	}
	
	/**
	 * Not available for END_OF_FILE
	 * 
	 */
	@Override
	public boolean addNote(String note) {
		throw new UnsupportedOperationException("No notes available in END_OF_FILE.");
	}
	
	/**
	 * Not available for END_OF_FILE
	 * 
	 */
	@Override
	public String getNote(int index) {
		throw new UnsupportedOperationException("No notes available in END_OF_FILE.");
	}
	

}
