/**
 *    Copyright 2014 Thomas Naeff (github.com/thnaeff)
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

import ch.thn.gedcom.creator.structures.AbstractGedcomStructure;
import ch.thn.gedcom.creator.structures.GedcomEOF;
import ch.thn.gedcom.creator.structures.GedcomFamily;
import ch.thn.gedcom.creator.structures.GedcomHeader;
import ch.thn.gedcom.creator.structures.GedcomIndividual;
import ch.thn.gedcom.creator.structures.GedcomSubmitter;
import ch.thn.gedcom.data.GedcomTree;
import ch.thn.gedcom.store.GedcomStore;

/**
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class GedcomCreatorUtil {
	
	private static int IDCount = 0;
	
	/**
	 * Creates the gedcom structure according to the type of the given gedcom head node
	 * 
	 * @param store
	 * @param gedcomHeadNode
	 * @return
	 */
	public static AbstractGedcomStructure gedcomCreatorStructureFactory(
			GedcomStore store, GedcomTree gedcomHeadNode) {
		
		String structureName = gedcomHeadNode.getStructureName();
		
		switch (structureName) {
		case AbstractGedcomStructure.END_OF_FILE:
			return new GedcomEOF(store, gedcomHeadNode);
		case AbstractGedcomStructure.FAM_RECORD:
			return new GedcomFamily(store, gedcomHeadNode);
		case AbstractGedcomStructure.HEADER:
			return new GedcomHeader(store, gedcomHeadNode);
		case AbstractGedcomStructure.INDIVIDUAL_RECORD:
			return new GedcomIndividual(store, gedcomHeadNode);
		case AbstractGedcomStructure.SUBMITTER_RECORD:
			return new GedcomSubmitter(store, gedcomHeadNode);
		default:
			return null;
		}
		
	}
	
	/**
	 * Add the abstract structure to the structure storage based on its type. The 
	 * structure ID will be used if != <code>null</code>, otherwise the structure 
	 * ID is used for structures which have an ID (like individuals or families) 
	 * and <code>null</code> will be used for all others
	 * 
	 * @param structureStorage
	 * @param structure
	 * @param structureId
	 */
	public static void addStructureBasedOnType(
			GedcomCreatorStructureStorage structureStorage, 
			AbstractGedcomStructure structure, String structureId) {
		
		String structureName = structure.getStructureName();
		
		switch (structureName) {
		case AbstractGedcomStructure.END_OF_FILE:
			if (structureId != null) {
				structureStorage.addEOF(structureId, (GedcomEOF)structure);
			} else {
				structureStorage.addEOF(String.valueOf(generateID()), (GedcomEOF)structure);
			}
			break;
		case AbstractGedcomStructure.FAM_RECORD:
			if (structureId != null) {
				structureStorage.addFamily(structureId, (GedcomFamily)structure);
			} else {
				structureStorage.addFamily((GedcomFamily)structure);
			}
			break;
		case AbstractGedcomStructure.HEADER:
			if (structureId != null) {
				structureStorage.addHeader(structureId, (GedcomHeader)structure);
			} else {
				structureStorage.addHeader(String.valueOf(generateID()), (GedcomHeader)structure);
			}
			break;
		case AbstractGedcomStructure.INDIVIDUAL_RECORD:
			if (structureId != null) {
				structureStorage.addIndividual(structureId, (GedcomIndividual)structure);
			} else {
				structureStorage.addIndividual((GedcomIndividual)structure);
			}
			break;
		case AbstractGedcomStructure.SUBMITTER_RECORD:
			if (structureId != null) {
				structureStorage.addSubmitter(structureId, (GedcomSubmitter)structure);
			} else {
				structureStorage.addSubmitter((GedcomSubmitter)structure);
			}
			break;
		default:
			throw new GedcomCreatorError("Unknown structure type " + structureName);
		}
		
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	private static int generateID() {
		return IDCount++;
	}

}
