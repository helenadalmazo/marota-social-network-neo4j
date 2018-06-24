package main.person;

import java.util.GregorianCalendar;
import java.util.List;


public class Person {
    private Long codigo;
    private String nome;
    private String email;
    private String dataNascimento;
    private String cidadeNascimento;
    private String cidadeResidencia;
    private List<Person> amigos;

    public Person () { }

    /**
     * @return the codigo
     */
    public Long getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the dataNascimento
     */
    public String getDataNascimento() {
        return dataNascimento;
    }

    /**
     * @param dataNascimento the dataNascimento to set
     */
    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    /**
     * @return the cidadeNascimento
     */
    public String getCidadeNascimento() {
        return cidadeNascimento;
    }

    /**
     * @param cidadeNascimento the cidadeNascimento to set
     */
    public void setCidadeNascimento(String cidadeNascimento) {
        this.cidadeNascimento = cidadeNascimento;
    }

    /**
     * @return the cidadeResidencia
     */
    public String getCidadeResidencia() {
        return cidadeResidencia;
    }

    /**
     * @param cidadeResidencia the cidadeResidencia to set
     */
    public void setCidadeResidencia(String cidadeResidencia) {
        this.cidadeResidencia = cidadeResidencia;
    }

    /**
     * @return the amigos
     */
    public List<Person> getAmigos() {
        return amigos;
    }

    /**
     * @param amigos the amigos to set
     */
    public void setAmigos(List<Person> amigos) {
        this.amigos = amigos;
    }
    
    public String toString() {
    	return this.codigo + " - " + this.getNome();    	
    }
}