package edu.xtec.qv.editor;

public class ManageQVTreeCellRenderer extends javax.swing.tree.DefaultTreeCellRenderer{

	public static javax.swing.ImageIcon assessmentIcon;
	public static javax.swing.ImageIcon assessmentSelIcon;
	public static javax.swing.ImageIcon sectionIcon;
	public static javax.swing.ImageIcon sectionSelIcon;
	public static javax.swing.ImageIcon itemIcon;
	public static javax.swing.ImageIcon assessmentManageIcon;
        public static javax.swing.ImageIcon assignManageIcon;
	public static javax.swing.ImageIcon studentIcon;
	public static javax.swing.ImageIcon groupIcon;
	public static javax.swing.ImageIcon schoolIcon;
	
	public ManageQVTreeCellRenderer(){
		assessmentIcon=new javax.swing.ImageIcon(ClassLoader.getSystemResource("edu/xtec/resources/icons/toc.gif"));
		assessmentSelIcon=new javax.swing.ImageIcon(ClassLoader.getSystemResource("edu/xtec/resources/icons/toc.gif"));
		sectionIcon=new javax.swing.ImageIcon(ClassLoader.getSystemResource("edu/xtec/resources/icons/cdataSectionNode.gif"));
		sectionSelIcon=new javax.swing.ImageIcon(ClassLoader.getSystemResource("edu/xtec/resources/icons/editorMode.gif"));
		itemIcon=new javax.swing.ImageIcon(ClassLoader.getSystemResource("edu/xtec/resources/icons/about.gif"));
		assessmentManageIcon=new javax.swing.ImageIcon(ClassLoader.getSystemResource("edu/xtec/resources/icons/index.gif"));
                assignManageIcon=new javax.swing.ImageIcon(ClassLoader.getSystemResource("edu/xtec/resources/icons/propertySheetSettings.gif"));
                studentIcon=new javax.swing.ImageIcon(ClassLoader.getSystemResource("edu/xtec/resources/icons/user.gif"));//"imatges/student2.gif");
		schoolIcon=new javax.swing.ImageIcon(ClassLoader.getSystemResource("edu/xtec/resources/icons/home16.gif"));
		groupIcon=new javax.swing.ImageIcon(ClassLoader.getSystemResource("edu/xtec/resources/icons/grup.gif"));
		/*assessmentIcon=new javax.swing.ImageIcon("imatges/assessment.gif");
		assessmentSelIcon=new javax.swing.ImageIcon("imatges/assessment_sel.gif");
		sectionIcon=new javax.swing.ImageIcon("imatges/section.gif");
		sectionSelIcon=new javax.swing.ImageIcon("imatges/section_sel.gif");
		itemIcon=new javax.swing.ImageIcon("imatges/item.gif");
		assessmentManageIcon=new javax.swing.ImageIcon("imatges/biblio.gif");*/
	}

	public java.awt.Component getTreeCellRendererComponent(javax.swing.JTree tree,
		Object value, boolean sel, boolean expanded, boolean leaf,
		int row, boolean hasFocus){
		
		super.getTreeCellRendererComponent(tree,value,sel,expanded,
			leaf,row,hasFocus);
		customizeIcon(value,sel);
		return this;
	}
	
	protected void customizeIcon(Object oNode, boolean bSelected){
		javax.swing.tree.DefaultMutableTreeNode node=(javax.swing.tree.DefaultMutableTreeNode)oNode;
		Object o=node.getUserObject();
		if (o instanceof ManageTreeQuaderns){
			ManageTreeQuaderns mtq=(ManageTreeQuaderns)o;
			if (mtq.isRoot()){ //Node Gestió de quaderns
				setIcon(assessmentManageIcon);
			}
			else{ 
				if (bSelected) setIcon(assessmentSelIcon);
				else setIcon(assessmentIcon);
			}
		}
		else if (o instanceof ManageTreeSection){
			if (bSelected) setIcon(sectionSelIcon);
			else setIcon(sectionIcon);
		}
		else if (o instanceof ManageTreeItem)
			setIcon(itemIcon);
                /*else if (o instanceof ManageTreeAssignacionsQuaderns)
                    setIcon(assignManageIcon);
                else if (o instanceof ManageTreeAssignacionsQuaderns)
                    setIcon(assignManageIcon);
                else if (o instanceof ManageTreeAssignacioQuadernAlumne || o instanceof ManageTreeAlumne || o instanceof ManageTreeDocent)
                    setIcon(studentIcon);
                else if (o instanceof ManageTreeGrupAlumnes && ((ManageTreeGrupAlumnes)o).isRoot())
                    setIcon(schoolIcon);
                else if (o instanceof ManageTreeGrupAlumnes || o instanceof ManageTreeGrupDocents || o instanceof ManageTreeAssignacioGrupAlumnesGrupDocents || o instanceof ManageTreeAssignacioQuadernGrupAlumnes)
                    setIcon(groupIcon);*/
		//System.out.println("name:"+o.getClass().getName());
	}
}