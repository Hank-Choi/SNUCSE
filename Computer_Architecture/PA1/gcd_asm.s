	.option nopic
	.attribute arch, "rv64i2p0_m2p0_a2p0_f2p0_d2p0_c2p0"
	.attribute unaligned_access, 0
	.attribute stack_align, 16
	.text


	.align	1
	.globl	gcd
	.type	gcd, @function
gcd:
	#------Your code starts here------
	#LHS: a0, RHS: a1
	addi t0,a0,0
	addi t1,a1,0
Loop:
	beq t0,t1,Exit
	bge t1,t0,Else
	sub t0,t0,t1
	j Loop
Else:
	sub t1,t1,t0
	j Loop
Exit:
	add a0,zero,t0
	#Load return value to reg a0
	#------Your code ends here------

	#Ret
	jr	ra
	.size	gcd, .-gcd


	.ident	"GCC: (GNU) 9.2.0"