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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import ch.thn.gedcom.creator.structures.GedcomEOF;
import ch.thn.gedcom.creator.structures.GedcomFamily;
import ch.thn.gedcom.creator.structures.GedcomHeader;
import ch.thn.gedcom.creator.structures.GedcomIndividual;
import ch.thn.gedcom.creator.structures.GedcomSubmitter;
import ch.thn.gedcom.data.GedcomNode;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

/**
 * This class can be used to collect all the gedcom creator structures and it provides 
 * the functionality to create family connections between all individuals and families 
 * (for example all children of an individual, all partners of an individual, ...). 
 * It also collects any missing individuals and families.
 * 
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class GedcomCreatorStructureStorage {
	
	private HashMap<String, GedcomEOF> eofs = null;
	private HashMap<String, GedcomHeader> headers = null;
	private HashMap<String, GedcomSubmitter> submitters = null;
	private HashMap<String, GedcomFamily> families = null;
	private HashMap<String, GedcomIndividual> individuals = null;
	
	private HashMultimap<String, GedcomFamily> familiesOfParent = null;
	private HashMultimap<String, GedcomFamily> familiesOfChild = null;
	
	private HashMultimap<String, GedcomIndividual> partnersOfIndividual = null;
	private HashMultimap<String, GedcomIndividual> childrenOfIndividual = null;
	
	private ArrayList<String> missingIndividuals = null;
	private ArrayList<String> missingFamilies = null;
	
	private boolean structuresModified = false;
	private boolean throwExceptionOnMissingStructures = false;
	
	/**
	 * 
	 */
	public GedcomCreatorStructureStorage() {
		
		eofs = new HashMap<>();
		headers = new HashMap<>();
		submitters = new HashMap<>();
		families = new HashMap<>();
		individuals = new HashMap<>();
		
		familiesOfParent = HashMultimap.create();
		familiesOfChild = HashMultimap.create();
		partnersOfIndividual = HashMultimap.create();
		childrenOfIndividual = HashMultimap.create();
		
		missingIndividuals = new ArrayList<>();
		missingFamilies = new ArrayList<>();
		
	}
	
	/**
	 * 
	 * 
	 * @param eofId
	 * @param eof
	 */
	public void addEOF(String eofId, GedcomEOF eof) {
		structuresModified = true;
		eofs.put(eofId, eof);
	}
	
	/**
	 * 
	 * 
	 * @param headerId
	 * @param header
	 */
	public void addHeader(String headerId, GedcomHeader header) {
		structuresModified = true;
		headers.put(headerId, header);
	}
	
	/**
	 * 
	 * 
	 * @param submitterId
	 * @param submitter
	 */
	public void addSubmitter(String submitterId, GedcomSubmitter submitter) {
		structuresModified = true;
		submitters.put(submitterId, submitter);
	}
	
	/**
	 * 
	 * 
	 * @param familyId Even though the family ID is already in the {@link GedcomFamily} 
	 * object and could be easily retrieved with {@link GedcomFamily#getId()}, the ID 
	 * can be given here if it is already available since that would save the getId-call 
	 * which might require some path following on the internal {@link GedcomNode}. If the 
	 * ID is not already available, the more convenient {@link #addFamily(GedcomFamily)} 
	 * method can be used.
	 * @param family
	 */
	public void addFamily(String familyId, GedcomFamily family) {
		structuresModified = true;
		families.put(familyId, family);
	}
	
	/**
	 * 
	 * 
	 * @param family
	 */
	public void addFamily(GedcomFamily family) {
		addFamily(family.getId(), family);
	}
	
	/**
	 * 
	 * 
	 * @param individualId Even though the individual ID is already in the {@link GedcomIndividual} 
	 * object and could be easily retrieved with {@link GedcomIndividual#getId()}, the ID 
	 * can be given here if it is already available since that would save the getId-call 
	 * which might require some path following on the internal {@link GedcomNode}. If the 
	 * ID is not already available, the more convenient {@link #addIndividual(GedcomIndividual)} 
	 * method can be used.
	 * @param individual
	 */
	public void addIndividual(String individualId, GedcomIndividual individual) {
		structuresModified = true;
		individuals.put(individualId, individual);
	}
	
	/**
	 * 
	 * 
	 * @param individual
	 */
	public void addIndividual(GedcomIndividual individual) {
		addIndividual(individual.getId(), individual);
	}
	
	/**
	 * 
	 * 
	 * @param eofId
	 * @return
	 */
	public GedcomEOF getEOF(String eofId) {
		return eofs.get(eofId);
	}
	
	/**
	 * 
	 * 
	 * @param headerId
	 * @return
	 */
	public GedcomHeader getHeader(String headerId) {
		return headers.get(headerId);
	}
	
	/**
	 * 
	 * 
	 * @param submitterId
	 * @return
	 */
	public GedcomSubmitter getSubmitter(String submitterId) {
		return submitters.get(submitterId);
	}
	
	/**
	 * 
	 * 
	 * @param familyId
	 * @return
	 */
	public GedcomFamily getFamily(String familyId) {
		return families.get(familyId);
	}
	
	/**
	 * 
	 * 
	 * @param individualId
	 * @return
	 */
	public GedcomIndividual getIndividual(String individualId) {
		return individuals.get(individualId);
	}
	
	/**
	 * 
	 * 
	 * @param eof
	 * @return
	 */
	public boolean hasEOF(GedcomEOF eof) {
		return eofs.values().contains(eof);
	}
	
	/**
	 * 
	 * 
	 * @param header
	 * @return
	 */
	public boolean hasHeader(GedcomHeader header) {
		return headers.values().contains(header);
	}
	
	/**
	 * 
	 * 
	 * @param submitter
	 * @return
	 */
	public boolean hasSubmitter(GedcomSubmitter submitter) {
		return submitters.values().contains(submitter);
	}
	
	/**
	 * 
	 * 
	 * @param family
	 * @return
	 */
	public boolean hasFamily(GedcomFamily family) {
		return families.values().contains(family);
	}
	
	/**
	 * 
	 * 
	 * @param individual
	 */
	public boolean hasIndividual(GedcomIndividual individual) {
		return individuals.values().contains(individual);
	}
	
	/**
	 * 
	 * 
	 * @param eofId
	 * @return
	 */
	public boolean hasEOF(String eofId) {
		return eofs.containsKey(eofId);
	}
	
	/**
	 * 
	 * 
	 * @param headerId
	 * @return
	 */
	public boolean hasHeader(String headerId) {
		return headers.containsKey(headerId);
	}
	
	/**
	 * 
	 * 
	 * @param submitterId
	 * @return
	 */
	public boolean hasSubmitter(String submitterId) {
		return submitters.containsKey(submitterId);
	}
	
	/**
	 * 
	 * 
	 * @param familyId
	 * @return
	 */
	public boolean hasFamily(String familyId) {
		return families.containsKey(familyId);
	}
	
	/**
	 * 
	 * 
	 * @param individualId
	 * @return
	 */
	public boolean hasIndividual(String individualId) {
		return individuals.containsKey(individualId);
	}
	
	/**
	 * 
	 * 
	 * @param eofId
	 * @return
	 */
	public GedcomEOF removeEOF(String eofId) {
		structuresModified = true;
		return eofs.remove(eofId);
	}
	
	/**
	 * 
	 * 
	 * @param headerId
	 * @return
	 */
	public GedcomHeader removeHeader(String headerId) {
		structuresModified = true;
		return headers.remove(headerId);
	}
	
	/**
	 * 
	 * 
	 * @param submitterId
	 * @return
	 */
	public GedcomSubmitter removeSubmitter(String submitterId) {
		structuresModified = true;
		return submitters.remove(submitterId);
	}
	
	/**
	 * 
	 * 
	 * @param familyId
	 * @return
	 */
	public GedcomFamily removeFamily(String familyId) {
		structuresModified = true;
		GedcomFamily removed = families.remove(familyId);
		
		if (removed != null) {
			//Clean up all families
			while (familiesOfParent.values().remove(removed)) {}
			while (familiesOfChild.values().remove(removed)) {}
		}
		
		return removed;
	}
	
	/**
	 * 
	 * 
	 * @param family
	 * @return
	 */
	public GedcomFamily removeFamily(GedcomFamily family) {
		return removeFamily(family.getId());
	}
	
	/**
	 * 
	 * 
	 * @param individualId
	 * @return
	 */
	public GedcomIndividual removeIndividual(String individualId) {
		structuresModified = true;
		GedcomIndividual removed = individuals.remove(individualId);
		
		if (removed != null) {
			//Clean up all individuals
			while (partnersOfIndividual.values().remove(removed)) {}
			while (childrenOfIndividual.values().remove(removed)) {}
		}
		
		return removed;
	}
	
	/**
	 * 
	 * 
	 * @param individual
	 * @return
	 */
	public GedcomIndividual removeIndividual(GedcomIndividual individual) {
		return removeIndividual(individual.getId());
	}
	
	/**
	 * 
	 * 
	 * @param individualId
	 */
	public Set<GedcomFamily> getFamiliesOfParent(String individualId) {
		return familiesOfParent.get(individualId);
	}
	
	/**
	 * 
	 * 
	 * @param individual
	 * @return
	 */
	public Set<GedcomFamily> getFamiliesOfParent(GedcomIndividual individual) {
		return getFamiliesOfParent(individual.getId());
	}
	
	/**
	 * 
	 * 
	 * @param individualId
	 */
	public Set<GedcomIndividual> getChildrenOfIndividual(String individualId) {
		return childrenOfIndividual.get(individualId);
	}
	
	/**
	 * 
	 * 
	 * @param individual
	 * @return
	 */
	public Set<GedcomIndividual> getChildrenOfIndividual(GedcomIndividual individual) {
		return getChildrenOfIndividual(individual.getId());
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public List<String> getMissingFamilies() {
		return missingFamilies;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public List<String> getMissingIndividuals() {
		return missingIndividuals;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean hasMissingStructures() {
		return missingIndividuals.size() > 0 || missingFamilies.size() > 0;
	}
	
	/**
	 * 
	 * 
	 * @param parent1Id
	 * @param parent2Id
	 * @return
	 */
	public GedcomFamily getFamilyOfParents(String parent1Id, String parent2Id) {
		Set<GedcomFamily> families1 = getFamiliesOfParent(parent1Id);
		Set<GedcomFamily> families2 = getFamiliesOfParent(parent2Id);
		
		//A comment in the guava docs: 
		//"I can use intersection as a Set directly, but copying it can be more 
		//efficient if I use it a lot."
		SetView<GedcomFamily> view = Sets.intersection(families1, families2);
		
		if (view.size() > 1) {
			throw new GedcomCreatorError("The parents " + parent1Id + " and " + parent2Id + 
					" have been found as parent in more than one family.");
		}
		
		//Returns the first family, or null if there is none
		return Iterables.getFirst(view, null);
	}
	
	/**
	 * 
	 * 
	 * @param parent1
	 * @param parent2
	 * @return
	 */
	public GedcomFamily getFamilyOfParents(GedcomIndividual parent1, GedcomIndividual parent2) {
		if (parent1 == null || parent2 == null) {
			return null;
		}
		
		return getFamilyOfParents(parent1.getId(), parent2.getId());
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public int getEOFCount() {
		return eofs.size();
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public int getHeaderCount() {
		return headers.size();
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public int getSubmitterCount() {
		return submitters.size();
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public int getFamilyCount() {
		return families.size();
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public int getIndividualCount() {
		return individuals.size();
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public int getTotalStructureCount() {
		return getEOFCount() + getHeaderCount() + getSubmitterCount() + 
				getFamilyCount() + getIndividualCount();
	}
	
	/**
	 * 
	 * 
	 */
	public void clearAll() {
		eofs.clear();
		headers.clear();
		submitters.clear();
		families.clear();
		individuals.clear();
		
		clearFamilyRelations();
	}
	
	/**
	 * 
	 * 
	 */
	public void clearFamilyRelations() {
		familiesOfParent.clear();
		familiesOfChild.clear();
		
		partnersOfIndividual.clear();
		childrenOfIndividual.clear();
		
		missingFamilies.clear();
		missingIndividuals.clear();
	}
	
	/**
	 * Returns <code>true</code> if a structure has been added or removed 
	 * after the last call to {@link #buildFamilyRelations()}.
	 * 
	 * @return
	 */
	public boolean structuresModified() {
		return structuresModified;
	}
	
	/**
	 * 
	 * 
	 */
	public void buildFamilyRelations() {
		clearFamilyRelations();
		
		for (GedcomIndividual indi : individuals.values()) {
			
			List<String> spouseFamilyLinks = indi.getSpouseFamilyLinks();
			for (String spouseFamilyLink : spouseFamilyLinks) {
				if (!families.containsKey(spouseFamilyLink)) {
					missingFamilies.add(spouseFamilyLink);
					if (throwExceptionOnMissingStructures) {
						throw new GedcomCreatorError("A spouse link to the family " + spouseFamilyLink + 
								" is listed in the individual " + indi.getId() + 
								", but such a family could not be found.");
					}
				}
			}
			
			List<String> childFamilyLinks = indi.getChildFamilyLinks();
			for (String childFamilyLink : childFamilyLinks) {
				if (!families.containsKey(childFamilyLink)) {
					missingFamilies.add(childFamilyLink);
					if (throwExceptionOnMissingStructures) {
						throw new GedcomCreatorError("A child link to the family " + childFamilyLink + 
								" is listed in the individual " + indi.getId() + 
								", but such a family could not be found.");
					}
				}
			}
			
		}
		
		
		for (GedcomFamily fam : families.values()) {
			
			String husbLink = fam.getHusbandLink();
			String wifeLink = fam.getWifeLink();
			
			familiesOfParent.put(husbLink, fam);
			familiesOfParent.put(wifeLink, fam);
			
			if (individuals.containsKey(husbLink)) {
				partnersOfIndividual.put(husbLink, individuals.get(husbLink));
			} else {
				missingIndividuals.add(husbLink);
				if (throwExceptionOnMissingStructures) {
					throw new GedcomCreatorError("A husband link to the individual " + husbLink + 
							" is listed in the family " + fam.getId() + 
							", but such an individual could not be found.");
				}
			}
			
			if (individuals.containsKey(wifeLink)) {
				partnersOfIndividual.put(wifeLink, individuals.get(wifeLink));
			} else {
				missingIndividuals.add(husbLink);
				if (throwExceptionOnMissingStructures) {
					throw new GedcomCreatorError("A wife link to the individual " + wifeLink + 
							" is listed in the family " + fam.getId() + 
							", but such an individual could not be found.");
				}
			}
			
			List<String> childLinks = fam.getChildLinks();
			for (String childLink : childLinks) {
				familiesOfChild.put(childLink, fam);
				
				if (individuals.containsKey(childLink)) {
					childrenOfIndividual.put(husbLink, individuals.get(childLink));
					childrenOfIndividual.put(wifeLink, individuals.get(childLink));
				} else {
					missingIndividuals.add(childLink);
					if (throwExceptionOnMissingStructures) {
						throw new GedcomCreatorError("A child link to the individual " + childLink + 
								" is listed in the family " + fam.getId() + 
								", but such an individual could not be found.");
					}
				}
			}
			
			
		}
		
		structuresModified = false;
		
	}

}
