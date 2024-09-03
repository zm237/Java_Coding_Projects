package forensic;


/**
* This class represents a forensic analysis system that manages DNA data using BSTs. Contains
* methods to create, read, update, delete, and flag profiles.
*
* @author Kal Pandit
*/
public class ForensicAnalysis {


 private TreeNode treeRoot; // BST's root
 private String firstUnknownSequence;
 private String secondUnknownSequence;


 public ForensicAnalysis() {
   treeRoot = null;
   firstUnknownSequence = null;
   secondUnknownSequence = null;
 }


 /**
  * Builds a simplified forensic analysis database as a BST and populates unknown sequences. The
  * input file is formatted as follows: 1. one line containing the number of people in the
  * database, say p 2. one line containing first unknown sequence 3. one line containing second
  * unknown sequence 2. for each person (p), this method: - reads the person's name - calls
  * buildSingleProfile to return a single profile. - calls insertPerson on the profile built to
  * insert into BST. Use the BST insertion algorithm from class to insert.
  *
  * <p>DO NOT EDIT this method, IMPLEMENT buildSingleProfile and insertPerson.
  *
  * @param filename the name of the file to read from
  */
 public void buildTree(String filename) {
   // DO NOT EDIT THIS CODE
   StdIn.setFile(filename); // DO NOT remove this line


   // Reads unknown sequences
   String sequence1 = StdIn.readLine();
   firstUnknownSequence = sequence1;
   String sequence2 = StdIn.readLine();
   secondUnknownSequence = sequence2;


   int numberOfPeople = Integer.parseInt(StdIn.readLine());


   for (int i = 0; i < numberOfPeople; i++) {
     // Reads name, count of STRs
     String fname = StdIn.readString();
     String lname = StdIn.readString();
     String fullName = lname + ", " + fname;
     // Calls buildSingleProfile to create
     Profile profileToAdd = createSingleProfile();
     // Calls insertPerson on that profile: inserts a key-value pair (name, profile)
     insertPerson(fullName, profileToAdd);
   }
 }


 /**
  * Reads ONE profile from input file and returns a new Profile. Do not add a StdIn.setFile
  * statement, that is done for you in buildTree.
  */
 public Profile createSingleProfile() {


   STR[] strs = new STR[StdIn.readInt()];


   for (int i = 0; i < strs.length; i++) {
     String str = StdIn.readString();
     int repeat = StdIn.readInt();
     STR add = new STR(str, repeat);
     strs[i] = add;
   }


   Profile profileToAdd = new Profile(strs);


   return profileToAdd; // update this line
 }


 /**
  * Inserts a node with a new (key, value) pair into the binary search tree rooted at treeRoot.
  *
  * <p>Names are the keys, Profiles are the values. USE the compareTo method on keys.
  *
  * @param newProfile the profile to be inserted
  */
 public void insertPerson(String name, Profile newProfile) {
   TreeNode nodeToAdd = new TreeNode();
   nodeToAdd.setName(name);
   nodeToAdd.setProfile(newProfile);


   if (treeRoot == null) {
     treeRoot = nodeToAdd;
     nodeToAdd.setLeft(null);
     nodeToAdd.setRight(null);
     return;
   }


   TreeNode ptr = treeRoot;
   TreeNode prev = null;
   int cmp = 0;


   while (ptr != null) {
     cmp = name.compareTo(ptr.getName());


     if (cmp == 0) {
       ptr = nodeToAdd;
       return;
     }


     prev = ptr;


     if (cmp < 0) ptr = ptr.getLeft();
     else ptr = ptr.getRight();
   }


   if (cmp < 0) prev.setLeft(nodeToAdd);
   else if (cmp > 0) prev.setRight(nodeToAdd);
 }


 private void search(TreeNode node, Queue<TreeNode> q, boolean truthValue) {
   if (node == null) return;
   search(node.getLeft(), q, truthValue);
   if (truthValue == true && node.getProfile().getMarkedStatus()) q.enqueue(node);
   else if (truthValue == false && node.getProfile().getMarkedStatus() == false) q.enqueue(node);
   search(node.getRight(), q, truthValue);
 }


 // private void search2(TreeNode node, Queue<TreeNode> q) {
 //   if (node == null) return;
 //   search(node.getLeft(), q);
 //   if (node.getProfile().getMarkedStatus() == false) q.enqueue(node);
 //   search(node.getRight(), q);
 // }


 /**
  * Finds the number of profiles in the BST whose interest status matches isOfInterest.
  *
  * @param isOfInterest the search mode: whether we are searching for unmarked or marked profiles.
  *     true if yes, false otherwise
  * @return the number of profiles according to the search mode marked
  */
 public int getMatchingProfileCount(boolean isOfInterest) {
   Queue<TreeNode> q = new Queue<>();
   int counter = 0;


   search(treeRoot, q, isOfInterest);


   while (!q.isEmpty()) {
     counter++;
     q.dequeue();
   }


   return counter; // update this line
 }


 /**
  * Helper method that counts the # of STR occurrences in a sequence. Provided method - DO NOT
  * UPDATE.
  *
  * @param sequence the sequence to search
  * @param STR the STR to count occurrences of
  * @return the number of times STR appears in sequence
  */
 private int numberOfOccurrences(String sequence, String STR) {


   // DO NOT EDIT THIS CODE


   int repeats = 0;
   // STRs can't be greater than a sequence
   if (STR.length() > sequence.length()) return 0;


   // indexOf returns the first index of STR in sequence, -1 if not found
   int lastOccurrence = sequence.indexOf(STR);


   while (lastOccurrence != -1) {
     repeats++;
     // Move start index beyond the last found occurrence
     lastOccurrence = sequence.indexOf(STR, lastOccurrence + STR.length());
   }
   return repeats;
 }


 private boolean check(TreeNode node) {
   STR[] array = node.getProfile().getStrs();
   double length = array.length;
   double half = Math.round(length / 2);
   int counter = 0;


   for (STR str : array) {
     if (str.getOccurrences()
         == numberOfOccurrences(firstUnknownSequence + secondUnknownSequence, str.getStrString()))
       counter++;
   }


   if (counter >= half) return true;


   return false;
 }


 private void flagProfilesOfInterest(TreeNode node) {
   if (node == null) return;


   flagProfilesOfInterest(node.getLeft());
   if (check(node)) node.getProfile().setInterestStatus(true);
   flagProfilesOfInterest(node.getRight());
 }


 /**
  * Traverses the BST at treeRoot to mark profiles if: - For each STR in profile STRs: at least
  * half of STR occurrences match (round UP) - If occurrences THROUGHOUT DNA (first + second
  * sequence combined) matches occurrences, add a match
  */
 public void flagProfilesOfInterest() {
   flagProfilesOfInterest(treeRoot);
 }


 /**
  * Uses a level-order traversal to populate an array of unmarked Strings representing unmarked
  * people's names. - USE the getMatchingProfileCount method to get the resulting array length. -
  * USE the provided Queue class to investigate a node and enqueue its neighbors.
  *
  * @return the array of unmarked people
  */
 public String[] getUnmarkedPeople() {
   int n = getMatchingProfileCount(false);
   String[] arr = new String[n];
   Queue<TreeNode> q = new Queue<>();
   int index = 0;


   if (treeRoot != null) q.enqueue(treeRoot);
   else return null;


   while (!q.isEmpty()) {
     TreeNode check = q.dequeue();


     if (check.getLeft() != null) q.enqueue(check.getLeft());
     if (check.getRight() != null) q.enqueue(check.getRight());


     if (check.getProfile().getMarkedStatus() == false) {
       arr[index] = check.getName();
       index++;
     }


     if (index >= n) break;
   }


   return arr; // update this line
 }


 private TreeNode findMin(TreeNode node) {
   TreeNode ptr = node;


   while (ptr.getLeft() != null) {
     ptr = ptr.getLeft();
   }


   return ptr;
 }


 private TreeNode deleteMin(TreeNode node) {
   if (node == null) return null;
   if (node.getLeft() == null) return node.getRight();
   node.setLeft(deleteMin(node.getLeft()));
   return node;
 }


 private void removePerson(TreeNode node, String key) {
   TreeNode ptrKey = treeRoot;
   String direction = null;
   TreeNode ptrPrev = ptrKey;


   while (ptrKey != null) {
     int cmp = key.compareTo(ptrKey.getName());


     if (cmp < 0) {
        ptrPrev = ptrKey;
       ptrKey = ptrKey.getLeft();
       direction = "Left";
     } else if (cmp > 0) {
        ptrPrev = ptrKey;
       ptrKey = ptrKey.getRight();
       direction = "Right";
     } else if (cmp == 0) break;
   }


   if (ptrKey == null) return;
   else if (ptrKey.equals(ptrPrev)) {
     if (ptrKey.getLeft() == null && ptrKey.getRight() == null) {
       treeRoot = null;
       return;
     } else if (ptrKey.getLeft() == null && ptrKey.getRight() != null) {
       treeRoot = ptrKey.getRight();
       return;
     } else if (ptrKey.getLeft() != null && ptrKey.getRight() == null) {
       treeRoot = ptrKey.getLeft();
       return;
     } else {
       TreeNode holder = ptrKey;
       TreeNode minNode = findMin(holder.getRight());


       holder.setRight(deleteMin(holder.getRight()));


       ptrKey.setName(minNode.getName());
       ptrKey.setProfile(minNode.getProfile());
       return;
     }
   } else {
     if (ptrKey.getLeft() == null && ptrKey.getRight() == null) {
       if (direction.equals("Right")) {
         ptrPrev.setRight(null);
         return;
       } else if (direction.equals("Left")) {
         ptrPrev.setLeft(null);
         return;
       }
     } else if (ptrKey.getLeft() == null && ptrKey.getRight() != null) {
       if (direction.equals("Right")) {
         ptrPrev.setRight(ptrKey.getRight());
         return;
       } else if (direction.equals("Left")) {
         ptrPrev.setLeft(ptrKey.getRight());
         return;
       }
     } else if (ptrKey.getLeft() != null && ptrKey.getRight() == null) {
       if (direction.equals("Right")) {
         ptrPrev.setRight(ptrKey.getLeft());
         return;
       } else if (direction.equals("Left")) {
         ptrPrev.setLeft(ptrKey.getLeft());
         return;
       }
     } else {
       TreeNode holder = ptrKey;
       TreeNode minNode = findMin(holder.getRight());


       holder.setRight(deleteMin(holder.getRight()));


       ptrKey.setName(minNode.getName());
       ptrKey.setProfile(minNode.getProfile());
     }
   }


   return;
 }



 /**
  * Removes a SINGLE node from the BST rooted at treeRoot, given a full name (Last, First) This is
  * similar to the BST delete we have seen in class.
  *
  * <p>If a profile containing fullName doesn't exist, do nothing. You may assume that all names
  * are distinct.
  *
  * @param fullName the full name of the person to delete
  */
 public void removePerson(String fullName) {
   TreeNode toFind = treeRoot;
   if (toFind == null) return;
   removePerson(toFind, fullName);
 }


 /**
  * Clean up the tree by using previously written methods to remove unmarked profiles. Requires the
  * use of getUnmarkedPeople and removePerson.
  */
 public void cleanupTree() {
   String[] peopleToRemove = getUnmarkedPeople();


   for (String name : peopleToRemove) {
     removePerson(name);
   }
 }


 /**
  * Gets the root of the binary search tree.
  *
  * @return The root of the binary search tree.
  */
 public TreeNode getTreeRoot() {
   return treeRoot;
 }


 /**
  * Sets the root of the binary search tree.
  *
  * @param newRoot The new root of the binary search tree.
  */
 public void setTreeRoot(TreeNode newRoot) {
   treeRoot = newRoot;
 }


 /**
  * Gets the first unknown sequence.
  *
  * @return the first unknown sequence.
  */
 public String getFirstUnknownSequence() {
   return firstUnknownSequence;
 }


 /**
  * Sets the first unknown sequence.
  *
  * @param newFirst the value to set.
  */
 public void setFirstUnknownSequence(String newFirst) {
   firstUnknownSequence = newFirst;
 }


 /**
  * Gets the second unknown sequence.
  *
  * @return the second unknown sequence.
  */
 public String getSecondUnknownSequence() {
   return secondUnknownSequence;
 }


 /**
  * Sets the second unknown sequence.
  *
  * @param newSecond the value to set.
  */
 public void setSecondUnknownSequence(String newSecond) {
   secondUnknownSequence = newSecond;
 }
}





