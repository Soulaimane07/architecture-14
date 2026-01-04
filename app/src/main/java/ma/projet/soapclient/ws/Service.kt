package ma.projet.soapclient.ws

import ma.projet.soapclient.beans.Compte
import ma.projet.soapclient.beans.TypeCompte
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import java.text.SimpleDateFormat
import java.util.*

/**
 * Extension function pour récupérer une propriété SOAP de manière sécurisée
 */
fun SoapObject.getPropertySafelyAsString(name: String): String {
    return try {
        this.getProperty(name)?.toString() ?: ""
    } catch (e: Exception) {
        ""
    }
}

/**
 * Classe Service pour gérer les interactions avec le service web SOAP
 */
class Service {
    // Namespace basé sur le package du backend: com.example.demo.ws
    private val NAMESPACE = "http://ws.demo.example.com/"
    // URL du service SOAP (10.0.2.2 = localhost pour l'émulateur Android)
    private val URL = "http://10.0.2.2:8080/services/BanqueWS"
    private val METHOD_GET_COMPTES = "getComptes"
    private val METHOD_CREATE_COMPTE = "createCompte"
    private val METHOD_DELETE_COMPTE = "deleteCompte"
    
    /**
     * Récupère la liste des comptes via le service SOAP.
     */
    fun getComptes(): List<Compte> {
        val request = SoapObject(NAMESPACE, METHOD_GET_COMPTES)
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11).apply {
            dotNet = false
            setOutputSoapObject(request)
        }
        val transport = HttpTransportSE(URL)
        val comptes = mutableListOf<Compte>()
        
        try {
            transport.call("", envelope)
            val response = envelope.bodyIn as SoapObject
            
            for (i in 0 until response.propertyCount) {
                val soapCompte = response.getProperty(i) as SoapObject
                val compte = Compte(
                    id = soapCompte.getPropertySafelyAsString("id").toLongOrNull(),
                    solde = soapCompte.getPropertySafelyAsString("solde").toDoubleOrNull() ?: 0.0,
                    dateCreation = try {
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(
                            soapCompte.getPropertySafelyAsString("dateCreation")
                        ) ?: Date()
                    } catch (e: Exception) {
                        Date()
                    },
                    type = try {
                        TypeCompte.valueOf(soapCompte.getPropertySafelyAsString("type"))
                    } catch (e: Exception) {
                        TypeCompte.COURANT
                    }
                )
                comptes.add(compte)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return comptes
    }
    
    /**
     * Crée un nouveau compte via le service SOAP.
     * @param solde Solde initial du compte.
     * @param type Type du compte (COURANT ou EPARGNE).
     */
    fun createCompte(solde: Double, type: TypeCompte): Boolean {
        val request = SoapObject(NAMESPACE, METHOD_CREATE_COMPTE).apply {
            addProperty("solde", solde)
            addProperty("type", type.name)
        }
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11).apply {
            dotNet = false
            setOutputSoapObject(request)
        }
        val transport = HttpTransportSE(URL)
        
        return try {
            transport.call("", envelope)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Supprime un compte en fonction de son ID via le service SOAP.
     * @param id Identifiant du compte à supprimer.
     */
    fun deleteCompte(id: Long): Boolean {
        val request = SoapObject(NAMESPACE, METHOD_DELETE_COMPTE).apply {
            addProperty("id", id)
        }
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11).apply {
            dotNet = false
            setOutputSoapObject(request)
        }
        val transport = HttpTransportSE(URL)
        
        return try {
            transport.call("", envelope)
            envelope.response as? Boolean ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
