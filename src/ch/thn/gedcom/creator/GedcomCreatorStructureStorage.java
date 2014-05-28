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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import ch.thn.gedcom.creator.structures.GedcomEOF;
import ch.thn.gedcom.creator.structures.GedcomFamily;
import ch.thn.gedcom.creator.structures.GedcomHeader;
import ch.thn.gedcom.creator.structures.GedcomIndividual;
import ch.thn.gedcom.creator.structures.GedcomSubmitter;
import ch.thn.gedcom.data.GedcomNode;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

/**
 * This class can be used to collect all the gedcom creator structures and it provides 
 * the functionality to create family connections between all individuals and families 
 * (for example all children of an individual, all partners of an individual, ...). 
 * It also collects any missing individuals and families.<br />
 * <br />
 * <br />
 * It uses a {@link TreeMap} for the storage of the structure keys and structures, 
 * which sorts the structures by the natural ordering of the keys (structure ID's).
 * 
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class GedcomCreatorStructureStorage {
	
	private TreeMap<String, GedcomEOF> eofs = null;
	private TreeMap<String, GedcomHeader> headers = null;
	private TreeMap<String, GedcomSubmitter> submitters = null;
	private TreeMap<String, GedcomFamily> families = null;
	private TreeMap<String, GedcomIndividual> individuals = null;
	
	/** All the parents and their families. Is updated each time a family is added and through {@link #buildFamilyRelations()} */
	private HashMultimap<String, GedcomFamily> familiesOfParent = null;
	/** All the children and the families where they are part of. Only available after a call to {@link #buildFamilyRelations()} */
	private HashMultimap<String, GedcomFamily> familiesOfChild = null;
	
	/** All the partners of an individual. Only available after a call to {@link #buildFamilyRelations()} */
	private HashMultimap<String, GedcomIndividual> partnersOfIndividual = null;
	/** All the children of an individual. Only available after a call to {@link #buildFamilyRelations()} */
	private HashMultimap<String, GedcomIndividual> childrenOfIndividual = null;
	
	/** Any individuals which are linked but do not exist. Only available after a call to {@link #buildFamilyRelations()} */
	private ArrayList<String> missingIndividuals = null;
	/** Any families which are linked but do not exist. Only available after a call to {@link #buildFamilyRelations()} */
	private ArrayList<String> missingFamilies = null;
	
	private boolean structuresModified = false;
	private boolean throwExceptionOnMissingStructures = false;
	
	/**
	 * 
	 */
	public GedcomCreatorStructureStorage() {
		
		eofs = new TreeMap<>();
		headers = new TreeMap<>();
		submitters = new TreeMap<>();
		families = new TreeMap<>();
		individuals = new TreeMap<>();
		
		familiesOfParent = HashMultimap.create();
		familiesOfChild = HashMultimap.create();
		partnersOfIndividual = HashMultimap.create();
		childrenOfIndividual = HashMultimap.create();
		
		missingIndividuals = new ArrayList<>();
		missingFamilies = new ArrayList<>();
		
	}
	
	/**
	 * Adds all the structures of the the given structure storage to this structure 
	 * storage.
	 * 
	 * @param structureStorage
	 */
	public void addAll(GedcomCreatorStructureStorage structureStorage) {
		add(structureStorage, true, true, true, true, true);
	}
	
	/**
	 * Adds all the structures of the the given structure storage to this structure 
	 * storage. The available flags can be used to choose the structures which 
	 * should be added.
	 * 
	 * @param structureStorage
	 * @param eof
	 * @param header
	 * @param submitter
	 * @param family
	 * @param individual
	 */
	public void add(GedcomCreatorStructureStorage structureStorage, 
			boolean eof, boolean header, boolean submitter, boolean family, boolean individual) {
		if (eof) {
			eofs.putAll(structureStorage.getModifiableEOFs());
		}
		
		if (header) {
			headers.putAll(structureStorage.getModifiableHeaders());
		}
		
		if (submitter) {
			submitters.putAll(structureStorage.getModifiableSubmitters());
		}
		
		if (family) {
			families.putAll(structureStorage.getModifiableFamilies());
		}
		
		if (individual) {
			individuals.putAll(structureStorage.getModifiableIndividuals());
		}
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	protected TreeMap<String, GedcomEOF> getModifiableEOFs() {
		return eofs;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	protected TreeMap<String, GedcomHeader> getModifiableHeaders() {
		return headers;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	protected TreeMap<String, GedcomSubmitter> getModifiableSubmitters() {
		return submitters;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	protected TreeMap<String, GedcomFamily> getModifiableFamilies() {
		return families;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	protected TreeMap<String, GedcomIndividual> getModifiableIndividuals() {
		return individuals;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public Set<String> getEOFIDs() {
		return eofs.keySet();
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public Set<String> getHeaderIDs() {
		return headers.keySet();
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public Set<String> getSubmitterIDs() {
		return submitters.keySet();
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public Set<String> getFamilyIDs() {
		return families.keySet();
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public Set<String> getIndividualIDs() {
		return individuals.keySet();
	}
	
	/**
	 * 
	 * 
	 * @param eofId
	 * @param eof
	 */
	public boolean addEOF(String eofId, GedcomEOF eof) {
		structuresModified = true;
		putIfNotNull(eofs, eofId, eof);
		return true;
	}
	
	/**
	 * 
	 * 
	 * @param headerId
	 * @param header
	 */
	public boolean addHeader(String headerId, GedcomHeader header) {
		structuresModified = true;
		putIfNotNull(headers, headerId, header);
		return true;
	}
	
	/**
	 * 
	 * 
	 * @param submitterId
	 * @param submitter
	 */
	public boolean addSubmitter(String submitterId, GedcomSubmitter submitter) {
		structuresModified = true;
		putIfNotNull(submitters, submitterId, submitter);
		return true;
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
	 * @return <code>false</code> if a family with the given ID already exists or 
	 * if there is already a family with the same husband and wife.
	 */
	public boolean addFamily(String familyId, GedcomFamily family) {
		if (familyId != null && families.containsKey(familyId)) {
			return false;
		}
		
		String husbLink = family.getHusbandLink();
		String wifeLink = family.getWifeLink();
		
		if (familiesOfParent.containsKey(husbLink) && familiesOfParent.containsKey(wifeLink)) {
			if (getFamilyOfParents(husbLink, wifeLink) != null) {
				//Both parents have a family already
				return false;
			}
		}
		
		putIfNotNull(familiesOfParent, husbLink, family);
		putIfNotNull(familiesOfParent, wifeLink, family);
		
		structuresModified = true;
		putIfNotNull(families, familyId, family);
		
		return true;
	}
	
	/**
	 * 
	 * 
	 * @param family
	 * @return <code>false</code> if a family with the given ID already exists or 
	 * if there is already a family with the same husband and wife.
	 */
	public boolean addFamily(GedcomFamily family) {
		return addFamily(family.getId(), family);
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
	 * @return <code>false</code> if an individual with the given ID already exists.
	 */
	public boolean addIndividual(String individualId, GedcomIndividual individual) {
		if (individualId != null && individuals.containsKey(individualId)) {
			return false;
		}
		
		structuresModified = true;
		putIfNotNull(individuals, individualId, individual);
		
		return true;
	}
	
	/**
	 * 
	 * 
	 * @param individual
	 * @return <code>false</code> if an individual with the same ID already exists.
	 */
	public boolean addIndividual(GedcomIndividual individual) {
		return addIndividual(individual.getId(), individual);
	}
	
	/**
	 * 
	 * 
	 * @param eofId
	 * @return
	 */
	public GedcomEOF getEOF(String eofId) {
		if (eofId == null) {
			return null;
		}
		
		return eofs.get(eofId);
	}
	
	/**
	 * 
	 * 
	 * @param headerId
	 * @return
	 */
	public GedcomHeader getHeader(String headerId) {
		if (headerId == null) {
			return null;
		}
		
		return headers.get(headerId);
	}
	
	/**
	 * 
	 * 
	 * @param submitterId
	 * @return
	 */
	public GedcomSubmitter getSubmitter(String submitterId) {
		if (submitterId == null) {
			return null;
		}
		
		return submitters.get(submitterId);
	}
	
	/**
	 * 
	 * 
	 * @param familyId
	 * @return
	 */
	public GedcomFamily getFamily(String familyId) {
		if (familyId == null) {
			return null;
		}
		
		return families.get(familyId);
	}
	
	/**
	 * 
	 * 
	 * @param individualId
	 * @return
	 */
	public GedcomIndividual getIndividual(String individualId) {
		if (individualId == null) {
			return null;
		}
		
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
		return eofId != null && eofs.containsKey(eofId);
	}
	
	/**
	 * 
	 * 
	 * @param headerId
	 * @return
	 */
	public boolean hasHeader(String headerId) {
		return headerId != null && headers.containsKey(headerId);
	}
	
	/**
	 * 
	 * 
	 * @param submitterId
	 * @return
	 */
	public boolean hasSubmitter(String submitterId) {
		return submitterId != null && submitters.containsKey(submitterId);
	}
	
	/**
	 * 
	 * 
	 * @param familyId
	 * @return
	 */
	public boolean hasFamily(String familyId) {
		return familyId != null && families.containsKey(familyId);
	}
	
	/**
	 * 
	 * 
	 * @param individualId
	 * @return
	 */
	public boolean hasIndividual(String individualId) {
		return individualId != null && individuals.containsKey(individualId);
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
	 * Returns an unmodifiable view on the EOF map
	 * 
	 * @return
	 */
	public Map<String, GedcomEOF> getEOFs() {
		return Collections.unmodifiableMap(eofs);
	}
	
	/**
	 * Returns an unmodifiable view on the HEADER map
	 * 
	 * @return
	 */
	public Map<String, GedcomHeader> getHeaders() {
		return Collections.unmodifiableMap(headers);
	}
	
	/**
	 * Returns an unmodifiable view on the SUBMITTER map
	 * 
	 * @return
	 */
	public Map<String, GedcomSubmitter> getSubmitters() {
		return Collections.unmodifiableMap(submitters);
	}
	
	/**
	 * Returns an unmodifiable view on the FAMILY map
	 * 
	 * @return
	 */
	public Map<String, GedcomFamily> getFamilies() {
		return Collections.unmodifiableMap(families);
	}
	
	/**
	 * Returns an unmodifiable view on the INDIVIDUAL map
	 * 
	 * @return
	 */
	public Map<String, GedcomIndividual> getIndividuals() {
		return Collections.unmodifiableMap(individuals);
	}
	
	
	/**
	 * 
	 * <b>Note:</b> Depends on the collection of information through {@link #buildFamilyRelations()}. 
	 * Calls {@link #buildFamilyRelations()} before returning the 
	 * result if any structures have been added or removed after the last call 
	 * to {@link #buildFamilyRelations()}, thus calling this method repeatedly 
	 * after adding/removing structures is very inefficient.
	 * 
	 * @param individualId
	 */
	public Set<GedcomFamily> getFamiliesOfParent(String individualId) {
		if (structuresModified) {
			buildFamilyRelations();
		}
		
		return familiesOfParent.get(individualId);
	}
	
	/**
	 * 
	 * 
	 * <b>Note:</b> Depends on the collection of information through {@link #buildFamilyRelations()}. 
	 * Calls {@link #buildFamilyRelations()} before returning the 
	 * result if any structures have been added or removed after the last call 
	 * to {@link #buildFamilyRelations()}, thus calling this method repeatedly 
	 * after adding/removing structures is very inefficient.
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
	 * <b>Note:</b> Depends on the collection of information through {@link #buildFamilyRelations()}. 
	 * Calls {@link #buildFamilyRelations()} before returning the 
	 * result if any structures have been added or removed after the last call 
	 * to {@link #buildFamilyRelations()}, thus calling this method repeatedly 
	 * after adding/removing structures is very inefficient.
	 * 
	 * @param individualId
	 */
	public Set<GedcomIndividual> getChildrenOfIndividual(String individualId) {
		if (structuresModified) {
			buildFamilyRelations();
		}
		
		return childrenOfIndividual.get(individualId);
	}
	
	/**
	 * 
	 * 
	 * <b>Note:</b> Depends on the collection of information through {@link #buildFamilyRelations()}. 
	 * Calls {@link #buildFamilyRelations()} before returning the 
	 * result if any structures have been added or removed after the last call 
	 * to {@link #buildFamilyRelations()}, thus calling this method repeatedly 
	 * after adding/removing structures is very inefficient.
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
	 * <b>Note:</b> Depends on the collection of information through {@link #buildFamilyRelations()}. 
	 * Calls {@link #buildFamilyRelations()} before returning the 
	 * result if any structures have been added or removed after the last call 
	 * to {@link #buildFamilyRelations()}, thus calling this method repeatedly 
	 * after adding/removing structures is very inefficient.
	 * 
	 * @return
	 */
	public List<String> getMissingFamilies() {
		if (structuresModified) {
			buildFamilyRelations();
		}
		
		return missingFamilies;
	}
	
	/**
	 * 
	 * 
	 * <b>Note:</b> Depends on the collection of information through {@link #buildFamilyRelations()}. 
	 * Calls {@link #buildFamilyRelations()} before returning the 
	 * result if any structures have been added or removed after the last call 
	 * to {@link #buildFamilyRelations()}, thus calling this method repeatedly 
	 * after adding/removing structures is very inefficient.
	 * 
	 * @return
	 */
	public List<String> getMissingIndividuals() {
		if (structuresModified) {
			buildFamilyRelations();
		}
		
		return missingIndividuals;
	}
	
	/**
	 * 
	 * 
	 * <b>Note:</b> Depends on the collection of information through {@link #buildFamilyRelations()}. 
	 * Calls {@link #buildFamilyRelations()} before returning the 
	 * result if any structures have been added or removed after the last call 
	 * to {@link #buildFamilyRelations()}, thus calling this method repeatedly 
	 * after adding/removing structures is very inefficient.
	 * 
	 * @return
	 */
	public boolean hasMissingStructures() {
		if (structuresModified) {
			buildFamilyRelations();
		}
		
		return missingIndividuals.size() > 0 || missingFamilies.size() > 0;
	}
	
	/**
	 * 
	 * 
	 * <b>Note:</b> Depends on the collection of information through {@link #buildFamilyRelations()}. 
	 * Calls {@link #buildFamilyRelations()} before returning the 
	 * result if any structures have been added or removed after the last call 
	 * to {@link #buildFamilyRelations()}, thus calling this method repeatedly 
	 * after adding/removing structures is very inefficient.
	 * 
	 * @param parent1Id
	 * @param parent2Id
	 * @return
	 */
	public GedcomFamily getFamilyOfParents(String parent1Id, String parent2Id) {
		if (structuresModified) {
			buildFamilyRelations();
		}
		
		Set<GedcomFamily> families1 = getFamiliesOfParent(parent1Id);
		Set<GedcomFamily> families2 = getFamiliesOfParent(parent2Id);
		
		//A comment in the guava docs: 
		//"I can use intersection as a Set directly, but copying it can be more 
		//efficient if I use it a lot."
		SetView<GedcomFamily> view = Sets.intersection(families1, families2);
		
		if (view.size() > 1) {
			throw new GedcomCreatorError("The parents " + parent1Id + " and " + parent2Id + 
					" have been found as parent in more than one (" + view.size() + ") family: " + view);
		}
		
		//Returns the first family, or null if there is none
		return Iterables.getFirst(view, null);
	}
	
	/**
	 * 
	 * 
	 * <b>Note:</b> Depends on the collection of information through {@link #buildFamilyRelations()}. 
	 * Calls {@link #buildFamilyRelations()} before returning the 
	 * result if any structures have been added or removed after the last call 
	 * to {@link #buildFamilyRelations()}, thus calling this method repeatedly 
	 * after adding/removing structures is very inefficient.
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
	 * Collects information about the relations between individuals/families.
	 * 
	 */
	public void buildFamilyRelations() {
		clearFamilyRelations();
		
		for (GedcomIndividual indi : individuals.values()) {
			
			//--- missingFamilies
			List<String> spouseFamilyLinks = indi.getSpouseFamilyLinks();
			for (String spouseFamilyLink : spouseFamilyLinks) {
				if (spouseFamilyLink != null && !families.containsKey(spouseFamilyLink)) {
					missingFamilies.add(spouseFamilyLink);
					if (throwExceptionOnMissingStructures) {
						throw new GedcomCreatorError("A spouse link to the family " + spouseFamilyLink + 
								" is listed in the individual " + indi.getId() + 
								", but such a family could not be found.");
					}
				}
			}
			
			//--- missingFamilies
			List<String> childFamilyLinks = indi.getChildFamilyLinks();
			for (String childFamilyLink : childFamilyLinks) {
				if (childFamilyLink != null && !families.containsKey(childFamilyLink)) {
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
			
			//--- familiesOfParent
			putIfNotNull(familiesOfParent, husbLink, fam);
			putIfNotNull(familiesOfParent, wifeLink, fam);
			
			//--- partnersOfIndividual
			//--- missingIndividuals
			if (husbLink != null && wifeLink != null && individuals.containsKey(husbLink)) {
				partnersOfIndividual.put(husbLink, individuals.get(wifeLink));
			} else {
				addIfNotNull(missingIndividuals, husbLink);
				if (throwExceptionOnMissingStructures) {
					throw new GedcomCreatorError("A husband link to the individual " + husbLink + 
							" is listed in the family " + fam.getId() + 
							", but such an individual could not be found.");
				}
			}
			
			if (wifeLink != null && husbLink != null && individuals.containsKey(wifeLink)) {
				partnersOfIndividual.put(wifeLink, individuals.get(husbLink));
			} else {
				addIfNotNull(missingIndividuals, husbLink);
				if (throwExceptionOnMissingStructures) {
					throw new GedcomCreatorError("A wife link to the individual " + wifeLink + 
							" is listed in the family " + fam.getId() + 
							", but such an individual could not be found.");
				}
			}
			
			//--- familiesOfChild
			//--- missingIndividuals
			List<String> childLinks = fam.getChildLinks();
			for (String childLink : childLinks) {
				putIfNotNull(familiesOfChild, childLink, fam);
				
				if (childLink != null && individuals.containsKey(childLink)) {
					putIfNotNull(childrenOfIndividual, husbLink, individuals.get(childLink));
					putIfNotNull(childrenOfIndividual, wifeLink, individuals.get(childLink));
				} else {
					addIfNotNull(missingIndividuals, childLink);
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
	
	/**
	 * 
	 * 
	 */
	public void cleanup() {
		
		Set<GedcomFamily> familiesToRemove = new HashSet<>();
		
		for (GedcomFamily family : families.values()) {
			String familyId = family.getId();
			String husbId = family.getHusbandLink();
			String wifeId = family.getWifeLink();
			
			boolean noHusbandLink = (husbId == null || husbId.length() == 0);
			boolean noWifeLink = (wifeId == null || wifeId.length() == 0);
			boolean noChildLinks = (family.getNumberOfChildren() == 0);
						
			if (noHusbandLink && noWifeLink) {
				//Family without parents
				familiesToRemove.add(family);
				
				List<String> childIds = family.getChildLinks();
				//Remove the family from all the children
				for (String childId : childIds) {
					individuals.get(childId).removeChildFamilyLink(familyId);
				}
			} else if (noChildLinks && (noWifeLink || noHusbandLink)) {
				//Family with a single parent and no children
				familiesToRemove.add(family);
				
				if (noHusbandLink) {
					//Remove family from wife
					individuals.get(wifeId).removeSpouseFamilyLink(familyId);
				} else if (noWifeLink) {
					//Remove family from husband
					individuals.get(husbId).removeSpouseFamilyLink(familyId);
				}
			}
			
		}
		
		families.values().removeAll(familiesToRemove);
		
		System.out.println("Cleanup: " + familiesToRemove.size() + " families removed.");
		
	}
	
	/**
	 * 
	 * 
	 * @param map
	 * @param key
	 * @param value
	 * @return
	 */
	private static boolean putIfNotNull(Multimap<String, GedcomFamily> map, 
			String key, GedcomFamily value) {
		if (key != null) {
			map.put(key,  value);
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * 
	 * @param map
	 * @param key
	 * @param value
	 * @return
	 */
	private static boolean putIfNotNull(Multimap<String, GedcomIndividual> map, 
			String key, GedcomIndividual value) {
		if (key != null) {
			map.put(key,  value);
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * 
	 * @param map
	 * @param key
	 * @param value
	 * @return
	 */
	private static boolean putIfNotNull(Map<String, GedcomEOF> map, 
			String key, GedcomEOF value) {
		if (key != null) {
			map.put(key,  value);
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * 
	 * @param map
	 * @param key
	 * @param value
	 * @return
	 */
	private static boolean putIfNotNull(Map<String, GedcomSubmitter> map, 
			String key, GedcomSubmitter value) {
		if (key != null) {
			map.put(key,  value);
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * 
	 * @param map
	 * @param key
	 * @param value
	 * @return
	 */
	private static boolean putIfNotNull(Map<String, GedcomHeader> map, 
			String key, GedcomHeader value) {
		if (key != null) {
			map.put(key,  value);
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * 
	 * @param map
	 * @param key
	 * @param value
	 * @return
	 */
	private static boolean putIfNotNull(Map<String, GedcomIndividual> map, 
			String key, GedcomIndividual value) {
		if (key != null) {
			map.put(key,  value);
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * 
	 * @param map
	 * @param key
	 * @param value
	 * @return
	 */
	private static boolean putIfNotNull(Map<String, GedcomFamily> map, 
			String key, GedcomFamily value) {
		if (key != null) {
			map.put(key,  value);
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param list
	 * @param value
	 * @return
	 */
	private static boolean addIfNotNull(List<String> list, String value) {
		if (value != null) {
			list.add(value);
			return true;
		}
		
		return false;
	}

}
