-- agents = {1 = "Abelard", 2 = "Heloise"}

-- propositions: rA, wA, rH, wH

-- some useful formulas
-- each person has only one hat
oneHat = "(rA = ~wA) & (rH = ~wH)"
-- there is only one white hat
oneWhite = "(wA > rH) & (wH > rA)"
-- Abelard can see Heloise's hat
abelardSees = "#_1 wH | #_1 rH"
-- Heloise can see Abelard's hat
heloiseSees = "#_2 wA | #_2 rA"
-- Abelard knows the color of his hat
abelardKnows = "#_1 wA | #_1 rA"
-- Heloise knows the color of her hat
heloiseKnows = "#_2 wH | #_2 rH"

-- make a conjunction
function conj(A, B)
	return oops.Formula("X & Y"):substitute({X = A, Y = B}, {})
end

-- make a "X knows that Y" formula
function knows(A, B)
	return oops.Formula("#_X Y"):substitute({Y = B}, {X = A})
end

-- two-deep "knows"
function knows2(A, B, C)
	return oops.Formula("#_X #_Y Z"):substitute(
		{Z = C}, {X = A, Y = B})
end

-- print knowledge state of Abelard
function abelardKnowledge(th)
	known = th:provable(abelardKnows)
	if (known) then
		print("Abelard knows")
	else
		print("Abelard doesn't know")
	end
end

-- print knowledge state of Heloise
function heloiseKnowledge(th)
	known = th:provable(heloiseKnows)
	if (known) then
		if (th:provable("#_2 rH")) then
			print ("Heloise knows her hat is red")
		elseif (th:provable("#_2 wH")) then
			print ("Heloise knows her hat is white")
		end
	else
		print("Heloise doesn't know")
	end
end

-- background knowledge: the possible hats + they see each other
background = conj(conj(oneHat, oneWhite), conj(abelardSees, heloiseSees))

-- add the background and the wise persons' knowledge about it
-- (to depth 2)
th = oops.Theory()
th:add(background)
th:add(knows("1", background))
th:add(knows("2", background))
th:add(knows2("2", "1", background))
th:add(knows2("1", "2", background))


oops.attachModelConstructor()
print("Initial Situation: ")
abelardKnowledge(th)
heloiseKnowledge(th)
-- oops.showModel()

-- After Abelard announces he doesn't know:
th:add(oops.Formula("#_2 ~ F"):substitute({F = abelardKnows}, {}))
th:add(oops.Formula("#_1 #_2 ~ F"):substitute({F = abelardKnows}, {}))


print()
print("After Abelard's announcement: ")
abelardKnowledge(th)
-- oops.showModel()
heloiseKnowledge(th)
