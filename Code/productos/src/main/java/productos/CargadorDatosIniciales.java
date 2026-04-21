package productos;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import modelo.Categoria;
import productos.repositoriosModelo.IRepositorioCategorias;

@Component
public class CargadorDatosIniciales implements CommandLineRunner {

    @Autowired
    private IRepositorioCategorias repositorioCategorias;

    @Override
    public void run(String... args) {
        try {
            if (repositorioCategorias.count() > 0) {
                System.out.println("Categorías ya existentes, omitiendo carga inicial.");
                return;
            }

            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] xmlFiles = resolver.getResources("classpath:CategoriasXML/*.xml");
            System.out.println("XMLs encontrados: " + xmlFiles.length);

            if (xmlFiles.length == 0) {
                System.err.println("AVISO: No se encontraron XMLs en classpath:CategoriasXML/");
                return;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            int cargadas = 0;
            for (Resource xml : xmlFiles) {
                try {
                    Document doc = builder.parse(xml.getInputStream());
                    Element raiz = doc.getDocumentElement();

                    NodeList hijos = raiz.getChildNodes();
                    String nombre = null;
                    for (int i = 0; i < hijos.getLength(); i++) {
                        if ("nombre".equals(hijos.item(i).getNodeName())) {
                            nombre = hijos.item(i).getTextContent().trim();
                            break;
                        }
                    }

                    if (nombre != null && !nombre.isEmpty()) {
                        Categoria cat = new Categoria(nombre, null, null);
                        repositorioCategorias.save(cat);
                        System.out.println("  Cargada: " + nombre);
                        cargadas++;
                    }
                } catch (Exception e) {
                    System.err.println("Error procesando " + xml.getFilename() + ": " + e.getMessage());
                }
            }

            System.out.println("Total categorias cargadas: " + cargadas);

        } catch (Exception e) {
            System.err.println("Error en CargadorDatosIniciales (app continua): " + e.getMessage());
        }
    }
}
