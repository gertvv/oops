
-- agents[1] = "Abelard", agents[2] = "Heloise"
agents = {"Abelard", "Heloise"}

-- propositions:
-- r1: Abelard wears a red hat
-- w1: Abelard wears the white hat
-- r2: Heloise wears a red hat
-- w2: Heloise wears the white hat

-- some useful formulas
-- each person has only one hat
oneHat = "(r1 = ~w1) & (r2 = ~w2)"
-- there is only one white hat
oneWhite = "(w1 > r2) & (w2 > r1)"
-- Abelard can see Heloise's hat
abelardSees = "#_1 w2 | #_1 r2"
-- Heloise can see Abelard's hat
heloiseSees = "#_2 w1 | #_2 r1"


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

-- print knowledge state of agent
function printKnowledge(th, agent)
    red = {"#_1 r1", "#_2 r2"}
    white = {"#_1 w1", "#_2 w2"}
    either = {red[1] .. " | " .. white[1], red[2] .. " | " .. white[2]}

    known = th:provable(either[agent])
    if (known) then
        if (th:provable(red[agent])) then
            print (agents[agent] .. " knows his/her hat is red")
        elseif (th:provable(white[agent])) then
            print (agents[agent] .. " knows his/her hat is white")
        end
    else
        print(agents[agent] .. " doesn't know")
    end
end

-- print knowledge state of Abelard
function printAbelard(th)
	printKnowledge(th, 1)
end

-- print knowledge state of Heloise
function printHeloise(th)
	printKnowledge(th, 2)
end

-- make sure we can show the counter model
oops.attachModelConstructor()

print("Initial Situation: ")
printAbelard(th)
printHeloise(th)
-- oops.showModel()

-- After Abelard announces he doesn't know:
abelardKnows = "#_1 w1 | #_1 r1"
th:add(oops.Formula("#_2 ~ F"):substitute({F = abelardKnows}, {}))

print()
print("After Abelard's announcement: ")
printAbelard(th)
oops.showModel()
printHeloise(th)
print("Consistent: " .. tostring(th:consistent()))
