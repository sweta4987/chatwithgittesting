

#program to implement binary search
def swe_binary_search(arr, l, r, x):
    if arr[mid] == x:
        return mid

    # If element is smaller than mid, then it can only be present in left subarray
    elif arr[mid] > x:
        return binary_search(arr, l, mid - 1, x)

    # Else the element can only be present in right subarray
    else:
        return binary_search(arr, mid + 1, r, x)


# Main Function
if __name__ == "__main__":
    # Function call
    result = swe_binary_search(arr, 0, len(arr) - 1, x)

    # printing the output
    if result != -1:
        print("Element is present at index {}".format(result))
    else:
        print("Element is not present in array")