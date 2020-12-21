package leetcode;

public class Test01 {
    static int[] nums = {1, 3, 13142, 1231, 43, 123, 2345, 6, 123123, 546456, 124, 456234, 123};

    static int target = 9;

    int[][] result;

    public static int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    int[] result = {i, j};
                    return result;
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(twoSum(nums, target));
    }


}
