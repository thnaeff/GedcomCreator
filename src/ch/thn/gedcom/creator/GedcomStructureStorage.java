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
public class GedcomStructureStorage {
	
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
	
	private boolean throwExceptionOnMissingStructures = false;
	
	/**
	 * 
	 */
	public GedcomStructureStorage() {
		
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
		eofs.put(eofId, eof);
	}
	
	/**
	 * 
	 * 
	 * @param headerId
	 * @param header
	 */
	public void addHeader(String headerId, GedcomHeader header) {
		headers.put(headerId, header);
	}
	
	/**
	 * 
	 * 
	 * @param submitterId
	 * @param submitter
	 */
	public void addSubmitter(String submitterId, GedcomSubmitter submitter) {
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
	 * @param eofId
	 * @return
	 */
	public GedcomEOF removeEOF(String eofId) {
		return eofs.remove(eofId);
	}
	
	/**
	 * 
	 * 
	 * @param headerId
	 * @return
	 */
	public GedcomHeader removeHeader(String headerId) {
		return headers.remove(headerId);
	}
	
	/**
	 * 
	 * 
	 * @param submitterId
	 * @return
	 */
	public GedcomSubmitter removeSubmitter(String submitterId) {
		return submitters.remove(submitterId);
	}
	
	/**
	 * 
	 * 
	 * @param familyId
	 * @return
	 */
	public GedcomFamily removeFamily(String familyId) {
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
		
	}

}
