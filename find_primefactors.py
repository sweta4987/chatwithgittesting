# Python program to find prime factors of a number

def swePrimeFactorscal(n): 
	productdata = 1
	
	for i in range(2, n+1): 
		if (n % i == 0): 
			isPrime = 1
			
			for j in range(2, int(i/2 + 1)): 
				if (i % j == 0): 
					isPrime = 0
					break
			if (isPrime): 
				productdata = productdata * i 
	return productdata

n = 5
print (swePrimeFactorscal(n)) 
