package clans;

import java.awt.Color;
import java.util.*;

/**
 * 
 * @author tancred
 */
public class ClusterWindow extends javax.swing.JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 8677383220559650947L;

    ClusteringWithGui parent;
    Vector<cluster> clusters;
    String[] clusternames;
    boolean didbootstrap = false;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addtoindividualseqgroupsbutton;
    private javax.swing.JButton addtoseqgroupsbutton;
    private javax.swing.JButton closebutton;
    private javax.swing.JList clusterlist;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane scrollpanel;
    // End of variables declaration//GEN-END:variables
    
    public ClusterWindow(ClusteringWithGui parent, Vector<cluster> clusters, String label, boolean didbootstrap) {
        this.parent = parent;
        this.clusters = clusters;
        this.didbootstrap = didbootstrap;
        this.clusternames = getclusternames(clusters);
        this.setTitle(label);
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        scrollpanel = new javax.swing.JScrollPane();
        clusterlist = new javax.swing.JList(clusternames);
        jPanel2 = new javax.swing.JPanel();
        addtoseqgroupsbutton = new javax.swing.JButton();
        addtoindividualseqgroupsbutton = new javax.swing.JButton();
        closebutton = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.X_AXIS));

        clusterlist.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                clusterlistValueChanged(evt);
            }
        });

        scrollpanel.setViewportView(clusterlist);

        jPanel1.add(scrollpanel);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        addtoseqgroupsbutton.setText("Add to sequence groups");
        addtoseqgroupsbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addtoseqgroupsbuttonActionPerformed(evt);
            }
        });

        jPanel2.add(addtoseqgroupsbutton);

        addtoindividualseqgroupsbutton.setText("Add each as separate sequence group");
        addtoindividualseqgroupsbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addtoindividualseqgroupsbuttonActionPerformed(evt);
            }
        });

        jPanel2.add(addtoindividualseqgroupsbutton);

        closebutton.setText("Close");
        closebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closebuttonActionPerformed(evt);
            }
        });

        jPanel2.add(closebutton);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }//GEN-END:initComponents
    
    /**
     * add each of the currently selected clusters as separate sequence group
     * @param evt
     */
    private void addtoindividualseqgroupsbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addtoindividualseqgroupsbuttonActionPerformed
        String newname=javax.swing.JOptionPane.showInputDialog(this,"Base group name:",this.getTitle());
        
        if(newname!=null && newname.length()==0){
            return;
        }
        
        int[] selectedvals=clusterlist.getSelectedIndices();
        
        if (selectedvals.length == 0) {
            return;
        }
        
        for (int i = selectedvals.length; --i >= 0;) {
            
            SequenceGroup newgroup = new SequenceGroup(newname + "_" + i, clusters.elementAt(selectedvals[i]).member,
                    parent.data.groupsize, 0, Color.red);

            parent.data.seqgroupsvec.addElement(newgroup);
        }
        
        parent.repaint();
    }//GEN-LAST:event_addtoindividualseqgroupsbuttonActionPerformed
    
    /**
     * add the currently selected groups to the seqgroups vector
     * @param evt
     */
    private void addtoseqgroupsbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addtoseqgroupsbuttonActionPerformed
        String newname=javax.swing.JOptionPane.showInputDialog(this,"Group name:","selected sequences");
        if(newname!=null && newname.length()==0){
            return;
        }
        int[] selectedvals=clusterlist.getSelectedIndices();
        if(java.lang.reflect.Array.getLength(selectedvals)==0){
            return;
        }
        int newsize=0;
        for(int i=java.lang.reflect.Array.getLength(selectedvals);--i>=0;){
            newsize+=((cluster)clusters.elementAt(selectedvals[i])).members();
        }//end for i
        //now add all the values to a new array
        int[] newselected=new int[newsize];
        int currcount=0;
        int mysize;
        int[] myarr;
        for(int i=java.lang.reflect.Array.getLength(selectedvals)-1;i>=0;i--){
            myarr=((cluster)clusters.elementAt(selectedvals[i])).member;
            mysize=java.lang.reflect.Array.getLength(myarr);
            for(int j=0;j<mysize;j++){
                newselected[j+currcount]=myarr[j];
            }//end for j
            currcount+=mysize;
        }//end for i
        
        SequenceGroup newgroup = new SequenceGroup(newname, newselected, parent.data.groupsize, 0, Color.red);
        parent.data.seqgroupsvec.addElement(newgroup);
        
        parent.repaint();
    }//GEN-LAST:event_addtoseqgroupsbuttonActionPerformed
    
    private void closebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closebuttonActionPerformed
        this.setVisible(false);
        dispose();
    }//GEN-LAST:event_closebuttonActionPerformed
    
    /**
     * update the selected sequences in parent
     * @param evt
     */
    private void clusterlistValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_clusterlistValueChanged
        int[] selectedvals=clusterlist.getSelectedIndices();
        int newsize=0;
        for(int i=java.lang.reflect.Array.getLength(selectedvals)-1;i>=0;i--){
            newsize+=((cluster)clusters.elementAt(selectedvals[i])).members();
        }//end for i
        //now add all the values to a new array
        int[] newselected=new int[newsize];
        float[] clusterconf=null;
        if(didbootstrap){
            //System.out.println("not using sequence confidences");
            clusterconf=new float[newsize];
        }
        int currcount=0;
        int mysize;
        int[] myarr;
        float[] myconf;
        for(int i=java.lang.reflect.Array.getLength(selectedvals)-1;i>=0;i--){
            myarr=((cluster)clusters.elementAt(selectedvals[i])).member;
            myconf=((cluster)clusters.elementAt(selectedvals[i])).seqconfidence;
            mysize=java.lang.reflect.Array.getLength(myarr);
            if(didbootstrap){//was: myconf!=null){
                for(int j=0;j<mysize;j++){
                    newselected[j+currcount]=myarr[j];
                    clusterconf[j+currcount]=myconf[j];
                }//end for j
            }else{
                for(int j=0;j<mysize;j++){
                    newselected[j+currcount]=myarr[j];
                }//end for j
            }
            currcount+=mysize;
        }//end for i
        parent.data.selectednames=newselected;
        parent.clusterconf=clusterconf;
        parent.repaint();
    }//GEN-LAST:event_clusterlistValueChanged
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    /**
     * convert the cluster to s.th. displayable as a String clustername and number of elements for example
     * 
     * @param invec
     * @return
     */
    String[] getclusternames(Vector<cluster> invec) {
        String[] retarr = new String[invec.size()];
        for (int i = 0; i < invec.size(); i++) {

            if (invec.elementAt(i).clusterconfidence > -1) {
                retarr[i] = invec.elementAt(i).name + " (" + invec.elementAt(i).members() + " sequences) (jacknife:"
                        + invec.elementAt(i).clusterconfidence * 100 + "%)";

            } else {
                retarr[i] = invec.elementAt(i).name + " (" + invec.elementAt(i).members() + " sequences)";
            }

        }
        return retarr;
    }
    
}//end clusterwindow
