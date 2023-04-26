package com.owl.api.example.service;

import com.owl.api.example.dto.RAMResponseDTO;
import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RAMService {

    private OntologyManager ontologyManager;

    private OWLDataProperty hasName;
    private OWLDataProperty hasType;
    private OWLDataProperty hasLatency;
    private OWLDataProperty hasCapacity;
    private OWLDataProperty hasVoltage;
    private OWLClassExpression classRAM;
    private OWLOntologyManager manager;
    private OWLDataFactory dataFactory;
    private OWLReasonerFactory reasonerFactory;
    private OWLReasoner reasoner;


    public RAMService() throws OWLOntologyCreationException, OWLOntologyStorageException {
        ontologyManager = new OntologyManager();
        manager = OWLManager.createOWLOntologyManager();
        dataFactory = manager.getOWLDataFactory();
        reasonerFactory = new ReasonerFactory();
        reasoner = reasonerFactory.createReasoner(this.ontologyManager.getOntology());
        hasName = dataFactory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/administrator/ontologies/2023/2/untitled-ontology-3#ram_has_name"));
        hasType = dataFactory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/administrator/ontologies/2023/2/untitled-ontology-3#ram_has_type"));
        hasLatency = dataFactory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/administrator/ontologies/2023/2/untitled-ontology-3#ram_has_latency"));
        hasVoltage = dataFactory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/administrator/ontologies/2023/2/untitled-ontology-3#ram_has_voltage"));
        hasCapacity = dataFactory.getOWLDataProperty(IRI.create("http://www.semanticweb.org/administrator/ontologies/2023/2/untitled-ontology-3#ram_has_capacity_in_gb"));
        classRAM = dataFactory.getOWLClass(IRI.create("http://www.semanticweb.org/administrator/ontologies/2023/2/untitled-ontology-3#RAM"));
    }

    public List<RAMResponseDTO> getAllRAMs(){
        return getRAMResponseDTOs(classRAM);
    }

    private List<RAMResponseDTO> getRAMResponseDTOs(OWLClassExpression classRAM) {
        Set<OWLNamedIndividual> individuals = reasoner.getInstances(classRAM, false).getFlattened();
        List<RAMResponseDTO> rams = new ArrayList<>();
        for (OWLNamedIndividual individual : individuals) {
            rams.add(setSpec(individual));
        }
        return rams;
    }

    private RAMResponseDTO setSpec(OWLNamedIndividual individual) {
        RAMResponseDTO ramResponseDTO = new RAMResponseDTO();
        for (OWLDataPropertyAssertionAxiom assertion : ontologyManager.getOntology().getDataPropertyAssertionAxioms(individual)) {
            OWLDataProperty property = assertion.getProperty().asOWLDataProperty();
            if (hasName.equals(property)) {
                OWLLiteral value = assertion.getObject();
                ramResponseDTO.setName(value.getLiteral());
            } else if (hasType.equals(property)) {
                OWLLiteral value = assertion.getObject();
                ramResponseDTO.setType(value.getLiteral());
            } else if (hasVoltage.equals(property)) {
                OWLLiteral value = assertion.getObject();
                ramResponseDTO.setVoltage(Double.parseDouble(value.getLiteral()));
            }else if (hasCapacity.equals(property)) {
                OWLLiteral value = assertion.getObject();
                ramResponseDTO.setCapacity(Integer.parseInt(value.getLiteral()));
            }else if (hasLatency.equals(property)) {
                OWLLiteral value = assertion.getObject();
                ramResponseDTO.setLatency(Integer.parseInt(value.getLiteral()));
            }
        }
        return ramResponseDTO;
    }
}
