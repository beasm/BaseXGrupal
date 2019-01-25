
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQResultSequence;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.xqj.basex.BaseXXQDataSource;

public class Principal {
	public static void main(String[] args) {
		XQDataSource xds = new BaseXXQDataSource();
		XQConnection con;
		XQExpression expr;
		XQResultSequence result;
		String sentencia;
		try {
			xds.setProperty("serverName", "localhost");
			xds.setProperty("port", "1984");
			con = xds.getConnection("admin", "admin");
		} catch (XQException e) {
			System.out.println("Error al establecer la conexión con BaseX");
			System.out.println(e.getMessage());
			return;
		}
		System.out.println("Establecida la conexión con BaseX");
		sentencia = "for $d in fn:collection('recibos')/recibos/recibo/detalle " + "order by $d/codigo " + "return $d";
		try {
			expr = con.createExpression();
			result = expr.executeQuery(sentencia);
		} catch (XQException e) {
			System.out.println("Error al ejecutar la sentencia XQuery");
			System.out.println(e.getMessage());
			return;
		}
		try {
			while (result.next()) {
				Node nodoProducto = result.getNode();
				mostrarProducto(nodoProducto);
				System.out.println();
			}
		} catch (XQException e) {
			System.out.println("Error al recorrer los elementos obtenidos");
			System.out.println(e.getMessage());
		}
		try {
			con.close();
		} catch (XQException e) {
			System.out.println("Error al cerrar la conexión con BaseX");
			System.out.println(e.getMessage());
		}
	}

	private static void mostrarProducto(Node nodo) {
		NodeList nodos = nodo.getChildNodes();
		for (int i = 0; i < nodos.getLength(); i++) {
			Node nodoHijo = nodos.item(i);
			if (nodoHijo.getNodeType() == Node.ELEMENT_NODE) {
				if (nodoHijo.getNodeName().equals("codigo")) {
					System.out.print(nodoHijo.getTextContent() + " ");
				}
				if (nodoHijo.getNodeName().equals("descripcion")) {
					System.out.print(nodoHijo.getTextContent() + " - ");
				}
				if (nodoHijo.getNodeName().equals("unidades")) {
					System.out.print(nodoHijo.getTextContent() + " ");
				}
				if (nodoHijo.getNodeName().equals("precio")) {
					System.out.print("unidades a " + nodoHijo.getTextContent());
				}
			}

		}
	}
}
